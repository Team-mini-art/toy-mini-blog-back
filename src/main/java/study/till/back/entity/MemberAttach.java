package study.till.back.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberAttach extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originFileName;

    private String savedFileName;

    private String uploadDir;

    private String extension;

    private Long size;

    private String contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


}
