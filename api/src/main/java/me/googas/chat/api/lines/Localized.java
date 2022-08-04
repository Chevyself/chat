package me.googas.chat.api.lines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.api.Channel;
import me.googas.chat.api.ResourceManager;
import me.googas.chat.api.lines.format.Formatter;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.commands.bukkit.utils.Components;
import me.googas.commands.util.Strings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

/** This is a {@link Line} which uses a message obtained from {@link ResourceManager}. */
public final class Localized implements Line {

  @NonNull @Getter private final Locale locale;
  @NonNull @Getter private final List<Line> extra;
  @NonNull private String json;

  Localized(@NonNull Locale locale, @NonNull String json) {
    this.locale = locale;
    this.json = json;
    this.extra = new ArrayList<>();
  }

  @Override
  public @NonNull Localized formatSample() {
    return (Localized) Line.super.formatSample();
  }

  @Override
  public @NonNull Localized formatSample(@NonNull Locale locale) {
    return (Localized) Line.super.formatSample(locale);
  }

  @Override
  public @NonNull Localized appendMany(@NonNull Collection<Line> extra) {
    return (Localized) Line.super.appendMany(extra);
  }

  @Override
  public @NonNull Localized appendMany(@NonNull Line... lines) {
    return (Localized) Line.super.appendMany(lines);
  }

  @Override
  public @NonNull String getRaw() {
    return json;
  }

  @NonNull
  public Localized setRaw(@NonNull String json) {
    this.json = json;
    return this;
  }

  @Override
  public @NonNull Localized copy() {
    return new Localized(locale, json).appendMany(this.extra);
  }

  @Override
  public @NonNull BaseComponent[] build() {
    List<BaseComponent> components = new ArrayList<>(Arrays.asList(Components.getComponent(json)));
    this.extra.forEach(line -> components.addAll(line.getComponents()));
    return components.toArray(new BaseComponent[0]);
  }

  @Override
  public BaseComponent[] build(@NonNull Channel channel) {
    List<BaseComponent> components = new ArrayList<>(Arrays.asList(Components.getComponent(json)));
    this.extra.forEach(line -> components.addAll(Arrays.asList(line.build(channel))));
    return components.toArray(new BaseComponent[0]);
  }

  @Override
  public @NonNull Optional<String> asText() {
    if (Line.isJson(this.json)) {
      return Optional.ofNullable(new TextComponent(this.build()).toLegacyText());
    } else {
      StringBuilder builder = new StringBuilder(BukkitUtils.format(this.json));
      this.extra.stream()
          .map(Line::asText)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .forEach(builder::append);
      return Optional.of(builder.toString());
    }
  }

  @Override
  public @NonNull Localized format(@NonNull Object... objects) {
    json = Strings.format(json, objects);
    this.extra.forEach(line -> line.format(objects));
    return this;
  }

  @Override
  public @NonNull Localized format(@NonNull Map<String, String> map) {
    json = Strings.format(json, map);
    this.extra.forEach(line -> line.format(map));
    return this;
  }

  @Override
  public @NonNull Localized format(@NonNull Formatter formatter) {
    return (Localized) formatter.format(this);
  }

  @Override
  public @NonNull Localized formatSample(@NonNull Channel channel) {
    return (Localized) Line.super.formatSample(channel);
  }

  @Override
  public @NonNull Localized append(@NonNull Line line) {
    this.extra.add(line);
    return this;
  }

  @Override
  public @NonNull Localized append(@NonNull String string) {
    return (Localized) Line.super.append(string);
  }

  /** Represents a formatter which can format {@link Line} using {@link Locale}. */
  public interface LocalizedFormatter {
    /**
     * Format the line.
     *
     * @param locale the locale to format the line with
     * @param line the line to format
     * @return the formatted line
     */
    @NonNull
    Line format(@NonNull Locale locale, @NonNull Line line);
  }
}
