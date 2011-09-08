package com.pv.trjira;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mysql.jdbc.PreparedStatement;

public class MySQLStmts {

	MySQLConnect connect = null;
	public static Log log = null;

	public MySQLStmts() {
		super();
		this.connect = new MySQLConnect();
		log = LogFactory.getLog(JFDDSync.class);
	}

	public Feature getFeature(String jiraID) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Feature feature = new Feature();

		try {

			stmt = (PreparedStatement) connect.getConnection().prepareStatement(
					"SELECT * FROM FDDPMA_FEATURE where JIRA_ID = ?");
			stmt.setString(1, jiraID);
			rs = stmt.executeQuery();

			/* Retrieve the data from the result set */
			while (rs.next()) {
				feature.setFeatureId(rs.getInt("FEATURE_ID"));
				feature.setWorkpackageId(rs.getInt("WORKPACKAGE_ID"));
				feature.setActivityId(rs.getInt("ACTIVITY_ID"));
				feature.setActivityId(rs.getInt("PROJECT_ID"));
				feature.setFeatureName(rs.getString("FEATURE_NAME"));
				feature.setDescription(rs.getString("DESCRIPTION"));
				feature.setReasonBehind(rs.getString("REASON_BEHIND"));
				feature.setSequence(rs.getInt("SEQUENCE"));
				feature.setCancelled(rs.getInt("CANCELLED"));
				feature.setOwnerUserId(rs.getInt("OWNER_USER_ID"));
				feature.setJiraId(rs.getString("JIRA_ID"));

			}

		} finally {
			/* Release the resources */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
					log.error("SQLException: " + sqlEx.getMessage());
				}
				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
					log.error("SQLException: " + sqlEx.getMessage());
				}

				stmt = null;
			}

		}
		return feature;
	}

	public List<FeatureMilestone> getMilstonesForFeature(int featureID) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FeatureMilestone milestone = null;
		List<FeatureMilestone> list = new ArrayList<FeatureMilestone>();

		try {

			stmt = (PreparedStatement) connect
					.getConnection()
					.prepareStatement(
							"SELECT FEATURE_ID, MILESTONE_ID, PLANNED_COMPLETION, ACTUAL_COMPLETION FROM fddpma_feature_milestone where FEATURE_ID = ? order by milestone_id");
			stmt.setInt(1, featureID);
			rs = stmt.executeQuery();

			/* Retrieve the data from the result set */
			while (rs.next()) {
				milestone = new FeatureMilestone();
				milestone.setFeatureId(rs.getInt("FEATURE_ID"));
				milestone.setFddpmaMilestoneID(rs.getInt("MILESTONE_ID"));
				milestone.setPlannedDate(rs.getDate("PLANNED_COMPLETION"));
				milestone.setActualDate(rs.getDate("ACTUAL_COMPLETION"));

				list.add(milestone);
			}

		} finally {
			/* Release the resources */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
					log.error("SQLException: " + sqlEx.getMessage());
				}
				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
					log.error("SQLException: " + sqlEx.getMessage());
				}
				stmt = null;
			}
		}
		return list;
	}

	public int getProject(String jiraKey) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int projectId = -1;

		try {

			stmt = (PreparedStatement) connect.getConnection().prepareStatement(
					"SELECT PROJECT_ID FROM FDDPMA_PROJECT where JIRA_KEY = ?");
			stmt.setString(1, jiraKey);
			rs = stmt.executeQuery();

			/* Retrieve the data from the result set */
			while (rs.next()) {
				projectId = rs.getInt("PROJECT_ID");
			}

		} finally {
			/* Release the resources */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
					log.error("SQLException: " + sqlEx.getMessage());
				}
				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
					log.error("SQLException: " + sqlEx.getMessage());
				}

				stmt = null;
			}

		}
		return projectId;
	}

	public int insertFeature(Feature feature) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int featureId = -1;

		try {

			stmt = (PreparedStatement) connect.getConnection().prepareStatement(
					"INSERT INTO FDDPMA_FEATURE (PROJECT_ID, FEATURE_NAME, CANCELLED, JIRA_ID) VALUES (?,?,?,?)");
			stmt.setInt(1, feature.getProjectId());
			stmt.setString(2, feature.getFeatureName());
			stmt.setInt(3, feature.getCancelled());
			stmt.setString(4, feature.getJiraId());
			stmt.executeUpdate();

			rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");

			if (rs.next()) {
				featureId = rs.getInt(1);
			} else {
				// throw an exception from here
			}

		} finally {
			/* Release the resources */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
					log.error("SQLException: " + sqlEx.getMessage());
				}
				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
					log.error("SQLException: " + sqlEx.getMessage());
				}

				stmt = null;
			}

		}
		return featureId;
	}

	public void insertMilestone(FeatureMilestone milestoneDate, Feature feature) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			StringBuffer sql = new StringBuffer("INSERT INTO fddpma_feature_milestone ");
			sql.append(" (FEATURE_ID, MILESTONE_ID, PLANNED_COMPLETION, ACTUAL_COMPLETION ) VALUES ");
			sql.append(" (?,?,?,?)");

			stmt = (PreparedStatement) connect.getConnection().prepareStatement(sql.toString());
			stmt.setInt(1, feature.getFeatureId());
			stmt.setInt(2, milestoneDate.getFddpmaMilestoneID());
			stmt.setDate(3, new Date(milestoneDate.getPlannedDate().getTime()));

			if (milestoneDate.getActualDate() == null) {
				stmt.setDate(4, null);
			} else {
				stmt.setDate(4, new Date(milestoneDate.getActualDate().getTime()));
			}
			stmt.executeUpdate();

		} finally {
			/* Release the resources */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
					log.error("SQLException: " + sqlEx.getMessage());
				}
				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
					log.error("SQLException: " + sqlEx.getMessage());
				}

				stmt = null;
			}

		}
	}

	public void updateMilestoneActualDate(FeatureMilestone milestoneDates, Feature feature) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = (PreparedStatement) connect
					.getConnection()
					.prepareStatement(
							"UPDATE fddpma_feature_milestone SET ACTUAL_COMPLETION  = ?	WHERE  FEATURE_ID = ? and MILESTONE_ID = ?");
			if (milestoneDates.getActualDate() == null) {
				stmt.setDate(1, null);
			} else {
				stmt.setDate(1, new Date(milestoneDates.getActualDate().getTime()));
			}
			stmt.setInt(2, feature.getFeatureId());
			stmt.setInt(3, milestoneDates.getFddpmaMilestoneID());
			stmt.executeUpdate();

		} finally {
			/* Release the resources */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
					log.error("SQLException: " + sqlEx.getMessage());
				}
				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
					log.error("SQLException: " + sqlEx.getMessage());
				}

				stmt = null;
			}

		}
	}

}
