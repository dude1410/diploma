package main.service;

import main.api.request.SettingsRequest;
import main.api.response.SettingsResponse;
import main.model.GlobalSettings;
import main.model.User;
import main.repository.GlobalSettingsRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static main.service.PostService.checkAuthorized;

@Service
public class SettingsService {

	private final GlobalSettingsRepository settingsRepository;
	private final UserRepository userRepository;

	@Autowired
	public SettingsService(GlobalSettingsRepository settingsRepository,
						   UserRepository userRepository) {
		this.settingsRepository = settingsRepository;
		this.userRepository = userRepository;
	}

	public SettingsResponse getGlobalSettings() {
		SettingsResponse settingsResponse = new SettingsResponse();
		List<GlobalSettings> settings = settingsRepository.findAll();

		for (GlobalSettings set : settings) {
			if (set.getCode().equals("MULTIUSER_MODE") && (set.getValue().equals("YES"))) {
				settingsResponse.setMultiuserMode(true);
			}

			if (set.getCode().equals("POST_PREMODERATION") && (set.getValue().equals("YES"))) {
				settingsResponse.setPostPremoderation(true);
			}

			if (set.getCode().equals("STATISTICS_IS_PUBLIC") && (set.getValue().equals("YES"))) {
				settingsResponse.setStatisticsIsPublic(true);
			}
		}
		return settingsResponse;
	}

	public void putSettings (SettingsRequest request) {

		String findEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		checkAuthorized(findEmail);
		User userInDB = userRepository.findByEmail(findEmail);
		if (userInDB == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		if (userInDB.isIs_moderator() == 0) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		List<GlobalSettings> settings = settingsRepository.findAll();

		for (GlobalSettings set : settings) {
			if (set.getCode().equals("MULTIUSER_MODE")) {
				set.setValue(request.isMultiuserMode() ? "YES" : "NO");
				settingsRepository.save(set);
			}
			if (set.getCode().equals("POST_PREMODERATION")) {
				set.setValue(request.isPostPremoderation() ? "YES" : "NO");
				settingsRepository.save(set);
			}
			if (set.getCode().equals("STATISTICS_IS_PUBLIC")) {
				set.setValue(request.isStatisticsIsPublic() ? "YES" : "NO");
				settingsRepository.save(set);
			}
		}
	}
}
