package org.infobip.mobile.messaging;

import android.support.annotation.NonNull;

import org.infobip.mobile.messaging.api.appinstance.AppInstanceWithPushRegId;
import org.infobip.mobile.messaging.api.appinstance.UserBody;
import org.infobip.mobile.messaging.util.DateTimeUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserDataMapper {

    public static UserBody toUserDataBody(UserData userData) {
        UserBody userBody = new UserBody();
        userBody.setExternalUserId(userData.getExternalUserId());
        userBody.setFirstName(userData.getFirstName());
        userBody.setLastName(userData.getLastName());
        userBody.setMiddleName(userData.getMiddleName());
        userBody.setBirthday(DateTimeUtil.DateToYMDString(userData.getBirthday()));
        if (userData.getGender() != null) {
            userBody.setGender(userData.getGender().name());
        }
        userBody.setEmails(userData.getEmailsWithPreferred());
        userBody.setGsms(userData.getGsmsWithPreferred());
        userBody.setTags(userData.getTags());
        userBody.setCustomAttributes(mapCustomAttsForBackendReport(userData.getCustomAttributes()));

        return userBody;
    }

    public static UserData createFrom(UserBody userResponse) {
        UserData userData = new UserData();

        if (userResponse.getExternalUserId() != null)
            userData.setExternalUserId(userResponse.getExternalUserId());
        if (userResponse.getFirstName() != null)
            userData.setFirstName(userResponse.getFirstName());
        if (userResponse.getLastName() != null)
            userData.setLastName(userResponse.getLastName());
        if (userResponse.getMiddleName() != null)
            userData.setMiddleName(userResponse.getMiddleName());
        if (userResponse.getBirthday() != null) {
            try {
                userData.setBirthday(DateTimeUtil.DateFromYMDString(userResponse.getBirthday()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (userResponse.getGender() != null)
            userData.setGender(UserData.Gender.valueOf(userResponse.getGender()));
        if (userResponse.getEmails() != null)
            userData.setEmailsWithPreferred(userResponse.getEmails());
        if (userResponse.getGsms() != null)
            userData.setGsmsWithPreferred(userResponse.getGsms());
        if (userResponse.getTags() != null)
            userData.setTags(userResponse.getTags());
        if (userResponse.getCustomAttributes() != null)
            userData.setCustomAttributes(mapCustomAttsFromBackendResponse(userResponse.getCustomAttributes()));
        if (userResponse.getInstances() != null)
            userData.setInstallations(mapInstancesToUserInstallations(userResponse));

        return userData;
    }

    private static List<UserData.Installation> mapInstancesToUserInstallations(UserBody userResponse) {
        List<UserData.Installation> installations = new ArrayList<>();
        for (AppInstanceWithPushRegId instance : userResponse.getInstances()) {
            installations.add(UserData.Installation.createFrom(instance));
        }
        return installations;
    }

    @NonNull
    public static Map<String, Object> mapCustomAttsForBackendReport(Map<String, CustomUserDataValue> customAttributes) {
        Map<String, Object> customAttributesToReport = new HashMap<>(customAttributes.size());
        for (Map.Entry<String, CustomUserDataValue> entry : customAttributes.entrySet()) {
            String key = entry.getKey();
            CustomUserDataValue value = entry.getValue();

            if (value == null) {
                customAttributesToReport.put(key, null);
            } else if (value.getType() == CustomUserDataValue.Type.Date) {
                customAttributesToReport.put(entry.getKey(), DateTimeUtil.DateToYMDString(value.dateValue()));
            } else if (value.getType() == CustomUserDataValue.Type.Number) {
                customAttributesToReport.put(key, value.numberValue());
            } else if (value.getType() == CustomUserDataValue.Type.String) {
                customAttributesToReport.put(key, value.stringValue());
            }
        }
        return customAttributesToReport;
    }

    public static Map<String, CustomUserDataValue> mapCustomAttsFromBackendResponse(Map<String, Object> customAttributes) {
        Map<String, CustomUserDataValue> customUserDataValueMap = new HashMap<>();
        if (customAttributes == null) {
            return customUserDataValueMap;
        }

        for (Map.Entry<String, Object> entry : customAttributes.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                String stringValue = (String) value;

                if (isPossibleDate(stringValue)) {
                    try {
                        Date dateValue = DateTimeUtil.DateFromYMDString(stringValue);
                        customUserDataValueMap.put(key, new CustomUserDataValue(dateValue));
                        continue;
                    } catch (ParseException ignored) {
                    }
                }
                customUserDataValueMap.put(key, new CustomUserDataValue(stringValue));

            } else if (value instanceof Number) {
                customUserDataValueMap.put(key, new CustomUserDataValue((Number) value));
            }
        }

        return customUserDataValueMap;
    }

    private static boolean isPossibleDate(String stringValue) {
        return Character.isDigit(stringValue.charAt(0)) && stringValue.length() == DateTimeUtil.DATE_YMD_FORMAT.length();
    }
}
