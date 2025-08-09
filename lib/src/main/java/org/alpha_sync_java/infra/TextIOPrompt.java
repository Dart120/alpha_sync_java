package org.alpha_sync_java.infra;

import org.alpha_sync_java.application.interfaces.PresentOption;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import java.util.Collection;
import java.util.List;

public class TextIOPrompt implements PresentOption {
    TextIO textIO = TextIoFactory.getTextIO();

    @Override
    public String withNumberedOptions(List<String> options, String prompt) {
        return textIO.newStringInputReader()
                .withPossibleValues(options)
                .read(prompt);
    }

    @Override
    public String simplePrompt(String prompt) {
        return textIO.newStringInputReader()
                .read(prompt);
    }
}
