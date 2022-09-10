package com.project.studytogether.controller.api;

import com.project.studytogether.dto.request.StudyCreateDto;
import com.project.studytogether.dto.response.ResponseDto;
import com.project.studytogether.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/study")
@RestController
public class StudyController {
	private final StudyService studyService;

	@PostMapping
	public ResponseDto<?> createStudy(@RequestBody StudyCreateDto studyCreateDto) {
		return studyService.createStudyGroup(studyCreateDto);
	}
}
