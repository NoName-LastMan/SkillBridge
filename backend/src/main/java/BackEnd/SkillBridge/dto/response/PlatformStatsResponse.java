package BackEnd.SkillBridge.dto.response;

import java.util.List;

/**
 * Response DTO untuk statistik aktivitas platform di Admin Dashboard.
 * Mengagregasi data dari semua entitas dalam sistem.
 */
public class PlatformStatsResponse {

    // ── User Metrics ──────────────────────────────────────────────────────
    private long totalUsers;
    private long totalStudents;
    private long verifiedStudents;
    private long unverifiedStudents;
    private long totalAdmins;

    // ── Project Metrics ───────────────────────────────────────────────────
    private long totalProjects;
    private long openProjects;
    private long closedProjects;
    private long completedProjects;

    // ── Application Metrics ───────────────────────────────────────────────
    private long totalApplications;
    private long pendingApplications;
    private long acceptedApplications;
    private long rejectedApplications;

    // ── Activity Metrics ──────────────────────────────────────────────────
    private long totalContactRequests;
    private long totalMessages;
    private long totalSkills;

    // ── Recent Activity Samples ───────────────────────────────────────────
    private List<AdminUserResponse> recentUsers;
    private List<ProjectSummary> recentProjects;

    // ── Nested: ProjectSummary ─────────────────────────────────────────────
    public static class ProjectSummary {
        private Long id;
        private String title;
        private String status;
        private String category;
        private String ownerEmail;
        private String ownerNama;

        public ProjectSummary() {}

        public ProjectSummary(Long id, String title, String status, String category,
                              String ownerEmail, String ownerNama) {
            this.id = id;
            this.title = title;
            this.status = status;
            this.category = category;
            this.ownerEmail = ownerEmail;
            this.ownerNama = ownerNama;
        }

        public Long getId()          { return id; }
        public String getTitle()     { return title; }
        public String getStatus()    { return status; }
        public String getCategory()  { return category; }
        public String getOwnerEmail(){ return ownerEmail; }
        public String getOwnerNama() { return ownerNama; }

        public void setId(Long id)               { this.id = id; }
        public void setTitle(String title)       { this.title = title; }
        public void setStatus(String status)     { this.status = status; }
        public void setCategory(String category) { this.category = category; }
        public void setOwnerEmail(String e)      { this.ownerEmail = e; }
        public void setOwnerNama(String n)       { this.ownerNama = n; }
    }

    // ── Constructors ───────────────────────────────────────────────────────
    public PlatformStatsResponse() {}

    // ── Getters & Setters ──────────────────────────────────────────────────
    public long getTotalUsers()              { return totalUsers; }
    public long getTotalStudents()           { return totalStudents; }
    public long getVerifiedStudents()        { return verifiedStudents; }
    public long getUnverifiedStudents()      { return unverifiedStudents; }
    public long getTotalAdmins()             { return totalAdmins; }
    public long getTotalProjects()           { return totalProjects; }
    public long getOpenProjects()            { return openProjects; }
    public long getClosedProjects()          { return closedProjects; }
    public long getCompletedProjects()       { return completedProjects; }
    public long getTotalApplications()       { return totalApplications; }
    public long getPendingApplications()     { return pendingApplications; }
    public long getAcceptedApplications()    { return acceptedApplications; }
    public long getRejectedApplications()    { return rejectedApplications; }
    public long getTotalContactRequests()    { return totalContactRequests; }
    public long getTotalMessages()           { return totalMessages; }
    public long getTotalSkills()             { return totalSkills; }
    public List<AdminUserResponse> getRecentUsers()       { return recentUsers; }
    public List<ProjectSummary>   getRecentProjects()     { return recentProjects; }

    public void setTotalUsers(long v)              { this.totalUsers = v; }
    public void setTotalStudents(long v)           { this.totalStudents = v; }
    public void setVerifiedStudents(long v)        { this.verifiedStudents = v; }
    public void setUnverifiedStudents(long v)      { this.unverifiedStudents = v; }
    public void setTotalAdmins(long v)             { this.totalAdmins = v; }
    public void setTotalProjects(long v)           { this.totalProjects = v; }
    public void setOpenProjects(long v)            { this.openProjects = v; }
    public void setClosedProjects(long v)          { this.closedProjects = v; }
    public void setCompletedProjects(long v)       { this.completedProjects = v; }
    public void setTotalApplications(long v)       { this.totalApplications = v; }
    public void setPendingApplications(long v)     { this.pendingApplications = v; }
    public void setAcceptedApplications(long v)    { this.acceptedApplications = v; }
    public void setRejectedApplications(long v)    { this.rejectedApplications = v; }
    public void setTotalContactRequests(long v)    { this.totalContactRequests = v; }
    public void setTotalMessages(long v)           { this.totalMessages = v; }
    public void setTotalSkills(long v)             { this.totalSkills = v; }
    public void setRecentUsers(List<AdminUserResponse> v) { this.recentUsers = v; }
    public void setRecentProjects(List<ProjectSummary> v) { this.recentProjects = v; }
}
