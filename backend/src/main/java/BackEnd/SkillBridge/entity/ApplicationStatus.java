package BackEnd.SkillBridge.entity;

public enum ApplicationStatus {
    PENDING,   // Menunggu keputusan ketua tim
    ACCEPTED,  // Diterima → otomatis jadi TeamMember
    REJECTED   // Ditolak
}
