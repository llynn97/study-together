package com.project.studytogether.service;

import com.project.studytogether.dto.request.StudyCreateDto;
import com.project.studytogether.dto.request.TagRequestDto;
import com.project.studytogether.dto.response.ResponseDto;
import com.project.studytogether.entity.Category;
import com.project.studytogether.entity.ChatRoom;
import com.project.studytogether.entity.Location;
import com.project.studytogether.entity.Study;
import com.project.studytogether.entity.StudyCount;
import com.project.studytogether.entity.StudyUser;
import com.project.studytogether.entity.Tag;
import com.project.studytogether.entity.User;
import com.project.studytogether.entity.enums.StudyUserRole;
import com.project.studytogether.entity.enums.UserMethod;
import com.project.studytogether.repository.CategoryRepository;
import com.project.studytogether.repository.ChatRoomRepository;
import com.project.studytogether.repository.LocationRepository;
import com.project.studytogether.repository.StudyCountRepository;
import com.project.studytogether.repository.StudyRepository;
import com.project.studytogether.repository.StudyUserRepository;
import com.project.studytogether.repository.TagRepository;
import com.project.studytogether.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StudyService {
	private final StudyRepository studyRepository;
	private final TagRepository tagRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final StudyUserRepository studyUserRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final StudyCountRepository studyCountRepository;
	private final LocationRepository locationRepository;

	/**
	 * study group 생성 method
	 * 1. 사용자로부터 스터디 정보 받아오기
	 * 2. 존재하지 않는 Tag 에 대해서는 새로 생성 후 향후 Study 와 연관지어 줄 예정.
	 * 3. Tag 를 Study 와 연관지어주기 위해서 Category 객체 사용. 생성후 Tag-Study 를 연관지어 준다.
	 * 4. 현재 Study group 을 생성하려는 사용자 계정을 study leader 로 정한 뒤 StudyUser 에 저장.
	 * 5. Chat room 생성
	 * 6. 대면/비대면 여부에 따라 스터디 개수 1 증가.
	 * 7. Study 생성
	 * @param studyCreateDto study info. tags, detail info, chat room
	 * @return success message
	 */
	@Transactional
	public ResponseDto<?> createStudyGroup(StudyCreateDto studyCreateDto) {
		Study study = studyCreateDto.toEntity();
		Location location = study.getLocation();
		location.setStudy(study);
		locationRepository.save(location);

		List<TagRequestDto> tagRequestDtoList = studyCreateDto.getTagRequestDtoList();
		List<Tag> studyTagList = new ArrayList<>();

		for (TagRequestDto tagRequestDto : tagRequestDtoList) {
			Optional<Tag> tagName = tagRepository.findByTagName(tagRequestDto.getTagName());
			if (tagName.isEmpty()) {
				Tag tag = Tag.builder()
						.tagName(tagRequestDto.getTagName())
						.build();
				tagRepository.save(tag);
				studyTagList.add(tag);
			}
		}

		for (Tag studyTag : studyTagList) {
			Category category = Category.builder()
					.study(study)
					.tag(studyTag)
					.build();
			categoryRepository.save(category);
		}

		String id = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> loginUser = userRepository.findById(id);
//		if (loginUser.isEmpty()) {
//			throw new IllegalArgumentException("존재하지 않는 로그인 계정입니다");
//		}

//		User studyLeader = loginUser.get();
		User studyLeader = User.builder()
				.email("rlawowns0@nver.com")
				.method(UserMethod.일반)
				.password("dljfsldf")
				.build();
		// .. test
		StudyUser studyUser = StudyUser.builder()
				.study(study)
				.user(studyLeader)
				.role(StudyUserRole.방장)
				.build();
		studyUserRepository.save(studyUser);

		ChatRoom chatRoom = ChatRoom.builder()
				.study(study)
				.createDate(LocalDateTime.now())
				.build();
		chatRoomRepository.save(chatRoom);

		Optional<StudyCount> counter = studyCountRepository.findByStudyMethod(study.getMethod());
		if (counter.isPresent()) {
			StudyCount studyCount = counter.get();
			studyCount.increase();
		}
		studyRepository.save(study);

		return new ResponseDto<>(200, "success to create study group");
	}
}
