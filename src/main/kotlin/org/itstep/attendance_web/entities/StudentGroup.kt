package org.itstep.attendance_web.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "student_groups")
data class StudentGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val groupId: Long? = null,

    val groupName: String,
    val groupShort: String,

    @OneToMany(mappedBy = "group", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnore
    val students: List<StudentInfo> = emptyList(),


    @ManyToMany
    @JoinTable(
        name = "group_subjects",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "subject_id")]
    )
    @JsonIgnore
    val subjects: List<StudentSubject>? = null
)
