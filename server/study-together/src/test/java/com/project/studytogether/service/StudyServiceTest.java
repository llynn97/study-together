package com.project.studytogether.service;

import com.project.studytogether.dto.request.StudyCreateDto;
import com.project.studytogether.dto.request.TagRequestDto;
import com.project.studytogether.dto.response.ResponseDto;
import com.project.studytogether.entity.Location;
import com.project.studytogether.entity.Study;
import com.project.studytogether.entity.enums.StudyMethod;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StudyServiceTest {
	@Autowired
	private StudyService studyService;

	@WithMockUser
	@Test
	void test() {
		Location location = Location.builder()
				.longitude((float) 123.123)
				.latitude((float) 321.321)
				.address("서울")
				.build();
		StudyCreateDto studyCreateDto = new StudyCreateDto();
		studyCreateDto.setHeadcount(4);
		studyCreateDto.setContent("hello this is CS study");
		studyCreateDto.setStudyMethod(StudyMethod.대면);
		studyCreateDto.setStartDate(LocalDate.now());
		studyCreateDto.setTitle("CS Study");
		studyCreateDto.setLocation(location);
		TagRequestDto tag1 = new TagRequestDto();
		tag1.setTagName("cs");
		TagRequestDto tag2 = new TagRequestDto();
		tag2.setTagName("it");
		studyCreateDto.setTagRequestDtoList(Arrays.asList(tag1, tag2));
		ResponseDto<?> response = studyService.createStudyGroup(studyCreateDto);
		Study data = (Study) response.getData();

		assertThat(data.getLocation()).isEqualTo(location);
		assertThat(data.getTitle()).isEqualTo("CS Study");
	}

}