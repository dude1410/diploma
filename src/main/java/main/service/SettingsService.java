package main.service;

import main.api.response.SettingsResponse;
import main.model.GlobalSettings;
import main.repository.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingsService {

	private final GlobalSettingsRepository settingsRepository;

	@Autowired
	public SettingsService (GlobalSettingsRepository settingsRepository) {
		this.settingsRepository = settingsRepository;
	}

	public SettingsResponse getGlobalSettings() {
		SettingsResponse settingsResponse = new SettingsResponse();
		List<GlobalSettings> settings = settingsRepository.findAll();

		if (settings.get(0).getValue().equals("YES")) {
			settingsResponse.setMultiuserMode(true);
		}

		if (settings.get(1).getValue().equals("YES")) {
			settingsResponse.setPostPremoderation(true);
		}

		if (settings.get(2).getValue().equals("YES")) {
			settingsResponse.setStatisticsIsPublic(true);
		}

		return settingsResponse;
	}

//	public SettingsResponse getGlobalSettings(){
//		SettingsResponse settingsResponse = new SettingsResponse();
//		settingsResponse.setMultiuserMode(true);
//		settingsResponse.setPostPremoderation(false);
//		settingsResponse.setStatisticsIsPublic(true);
//		return settingsResponse;
//	}
}
