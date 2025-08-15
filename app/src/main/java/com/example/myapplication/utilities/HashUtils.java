package com.example.myapplication.utilities;

import com.example.myapplication.domain.Activity;
import com.example.myapplication.domain.dto.GuestResponse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

public class HashUtils {
    public static String hashActivityList(List<Activity> activities) {
        StringBuilder sb = new StringBuilder();

        // Sort using meaningful fields to ensure consistent order
        activities.stream()
                .sorted((a1, a2) -> {
                    int cmp;
                    cmp = safeCompare(a1.getName(), a2.getName()); if (cmp != 0) return cmp;
                    cmp = safeCompare(a1.getPlace(), a2.getPlace()); if (cmp != 0) return cmp;
                    cmp = safeCompare(a1.getDescription(), a2.getDescription()); if (cmp != 0) return cmp;
                    cmp = safeCompare(a1.getStartTime(), a2.getStartTime()); if (cmp != 0) return cmp;
                    return safeCompare(a1.getEndTime(), a2.getEndTime());
                })
                .forEach(activity -> sb.append(
                        safe(activity.getName()) + "|" +
                                safe(activity.getPlace()) + "|" +
                                safe(activity.getDescription()) + "|" +
                                safe(activity.getStartTime()) + "|" +
                                safe(activity.getEndTime()) + ";"
                ));

        return sha256(sb.toString());
    }

    public static String hashGuestList(List<GuestResponse> guests) {
        StringBuilder sb = new StringBuilder();

        guests.stream()
                .sorted((g1, g2) -> {
                    int cmp = safeCompare(g1.getName(), g2.getName());
                    if (cmp != 0) return cmp;
                    return safeCompare(g1.getEmail(), g2.getEmail());
                })
                .forEach(guest -> sb.append(
                        safe(guest.getName()) + "|" +
                                safe(guest.getEmail()) + ";"
                ));

        return sha256(sb.toString());
    }


    private static int safeCompare(String a, String b) {
        return safe(a).compareTo(safe(b));
    }

    private static String safe(String value) {
        return value != null ? value : "";
    }

    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hash); // or use Hex format if preferred
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }


}

