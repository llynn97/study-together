package com.project.studytogether.dto.request;

import com.project.studytogether.entity.Location;
import com.project.studytogether.entity.Study;
import com.project.studytogether.entity.enums.StudyMethod;
import com.project.studytogether.entity.enums.StudyStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * {
 *   "title" : "CS 스터디",
 *   "head_count" : 2,
 *   "study_status" : "RECRUITING",
 *   "start_date" : 2022-07-21,
 *   "method" : "OFFLINE",
 *   "content" : "안녕하세요. CS 기술 면접을 준비하기 위한 면접 스터디를 진행하려고 합니다. 많은 관심 부탁드립니다."
 *   "location" : {
 *     "address" : "서울시 종로구 xxx",
 *     "latitude" : 123xxx,
 *     "longitude" : xxxx
 *   }
 * }
 */

@Data
public class StudyCreateDto {
	private String title;
	private int headcount;
	private StudyMethod studyMethod;

	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate startDate;
	private String content;
	private Location location;
	private List<TagRequestDto> tagRequestDtoList;

	public Study toEntity() {
		return Study.builder()
				.title(title)
				.headcount(headcount)
				.method(studyMethod)
				.studyStatus(StudyStatus.모집중)
				.startDate(startDate)
				.content(content)
				.location(location)
				.writtenDate(LocalDateTime.now())
				.build();
	}
}
