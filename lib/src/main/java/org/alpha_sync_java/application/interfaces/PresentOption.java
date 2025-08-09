package org.alpha_sync_java.application.interfaces;

import java.util.List;

public interface PresentOption {
    String withNumberedOptions(List<String> options, String prompt);
    String simplePrompt(String prompt);
}
