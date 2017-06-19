package edu.wpi.first.shuffleboard.data;

import edu.wpi.first.shuffleboard.util.Maps;

import java.util.Arrays;
import java.util.Map;

/**
 * Represents data options sent by the robot that may be selected by the drivers.
 */
public final class SendableChooserData extends ComplexData<SendableChooserData> {

  public static final String OPTIONS_KEY = "options";
  public static final String DEFAULT_OPTION_KEY = "default";
  public static final String SELECTED_OPTION_KEY = "selected";

  private final String[] options;
  private final String defaultOption;
  private final String selectedOption;

  /**
   * Creates a new sendable chooser data object backed by the given map.
   *
   * @param map the map backing the data
   */
  public SendableChooserData(Map<String, Object> map) {
    this((String[]) map.getOrDefault(OPTIONS_KEY, new String[0]),
        (String) map.getOrDefault(DEFAULT_OPTION_KEY, ""),
        (String) map.getOrDefault(SELECTED_OPTION_KEY, ""));
  }

  @SuppressWarnings("JavadocMethod")
  public SendableChooserData(String[] options, String defaultOption, String selectedOption) {
    this.options = options.clone();
    this.defaultOption = defaultOption;
    this.selectedOption = selectedOption;
  }

  public String[] getOptions() {
    return options.clone();
  }

  public String getDefaultOption() {
    return defaultOption;
  }

  public String getSelectedOption() {
    return selectedOption;
  }

  public SendableChooserData withOptions(String... options) {
    return new SendableChooserData(options, this.defaultOption, this.selectedOption);
  }

  public SendableChooserData withDefaultOption(String defaultOption) {
    return new SendableChooserData(this.options, defaultOption, this.selectedOption);
  }

  public SendableChooserData withSelectedOption(String selectedOption) {
    return new SendableChooserData(this.options, this.defaultOption, selectedOption);
  }

  @Override
  public String toString() {
    return String.format("SendableChooserData(options=%s, defaultOption=%s, selectedOption=%s)",
        Arrays.toString(options), defaultOption, selectedOption);
  }

  @Override
  public Map<String, Object> asMap() {
    return Maps.<String, Object>builder()
        .put(OPTIONS_KEY, getOptions())
        .put(DEFAULT_OPTION_KEY, getDefaultOption())
        .put(SELECTED_OPTION_KEY, getSelectedOption())
        .build();
  }

}