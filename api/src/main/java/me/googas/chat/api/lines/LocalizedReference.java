package me.googas.chat.api.lines;

import java.util.*;
import java.util.logging.Level;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.ErrorHandler;
import me.googas.chat.api.Channel;
import me.googas.chat.api.Language;
import me.googas.chat.api.ResourceManager;
import me.googas.chat.api.lines.format.Formatter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;

/** A {@link Line} that references a language key to be built into {@link Localized}. */
public final class LocalizedReference implements Line {

  /** Objects formatters. */
  @NonNull private final List<Object> objects;
  /** Placeholders formatters. */
  @NonNull private final Map<String, String> placeholders;
  /** Formatters. */
  @NonNull private final List<Formatter> formatters;

  @NonNull private final List<Placeholder> linePlaceholder;

  @NonNull @Getter private final List<Line> extra;

  @NonNull private String key;

  LocalizedReference(@NonNull String key) {
    this(
        new ArrayList<>(),
        new HashMap<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        key);
  }

  private LocalizedReference(
      @NonNull List<Object> objects,
      @NonNull Map<String, String> placeholders,
      @NonNull List<Formatter> formatters,
      @NonNull List<Placeholder> linePlaceholder,
      @NonNull List<Line> extra,
      @NonNull String key) {
    this.objects = objects;
    this.placeholders = placeholders;
    this.formatters = formatters;
    this.linePlaceholder = linePlaceholder;
    this.extra = extra;
    this.key = key;
  }

  /**
   * Get the {@link Localized} that this references to.
   *
   * @param locale the locale to get the raw message
   * @return the {@link Localized}
   */
  public @NonNull Localized asLocalized(@NonNull Locale locale) {
    Localized localized = Line.localized(locale, this.key);
    if (!extra.isEmpty()) localized.appendMany(this.extra);
    if (!objects.isEmpty()) localized.format(objects.toArray());
    if (!placeholders.isEmpty()) localized.format(placeholders);
    if (!formatters.isEmpty()) localized.format(formatters);
    if (!linePlaceholder.isEmpty())
      localized.placeholders(linePlaceholder.toArray(new Placeholder[0]));
    return localized;
  }

  /**
   * Get the {@link Localized} that this references to.
   *
   * @param sender the sender to get the locale from
   * @return the {@link Localized}
   */
  public @NonNull Localized asLocalized(@NonNull CommandSender sender) {
    return this.asLocalized(Language.getLocale(sender));
  }

  /**
   * Get the {@link Localized} that this references to.
   *
   * @param channel the channel to get the locale from
   * @return the {@link Localized}
   */
  public @NonNull Localized asLocalized(@NonNull Channel channel) {
    return this.asLocalized(channel.getLocale().orElse(ResourceManager.getBase()));
  }

  /**
   * Raw use of {@link Localized}. This will warn the {@link java.util.logging.Logger} when used
   *
   * @return the {@link Localized}
   */
  public @NonNull Localized asLocalized() {
    return this.asLocalized(ResourceManager.getBase());
  }

  @Override
  public @NonNull LocalizedReference copy() {
    return new LocalizedReference(
        new ArrayList<>(this.objects),
        new HashMap<>(this.placeholders),
        new ArrayList<>(this.formatters),
        linePlaceholder,
        this.extra,
        this.key);
  }

  @Override
  public @NonNull LocalizedReference appendMany(@NonNull Collection<Line> extra) {
    return (LocalizedReference) Line.super.appendMany(extra);
  }

  @Override
  public @NonNull LocalizedReference appendMany(@NonNull Line... lines) {
    return (LocalizedReference) Line.super.appendMany(lines);
  }

  @Override
  public @NonNull BaseComponent[] build() {
    ErrorHandler.getInstance().handle(Level.FINEST, "Raw use of LocalizedReference#build");
    return this.asLocalized().build();
  }

  @Override
  public @NonNull BaseComponent[] build(boolean sample) {
    ErrorHandler.getInstance().handle(Level.FINEST, "Raw use of LocalizedReference#build");
    return this.asLocalized().build(sample);
  }

  @Override
  public BaseComponent[] build(@NonNull Channel channel, boolean placeholders, boolean sample) {
    return this.asLocalized(channel).build(channel, placeholders, sample);
  }

  @Override
  public @NonNull String asText(@NonNull Channel channel, boolean placeholders, boolean sample) {
    return this.asLocalized(channel).asText(channel, placeholders, sample);
  }

  @Override
  public @NonNull String asText(boolean sample) {
    ErrorHandler.getInstance().handle(Level.FINEST, "Raw use of LocalizedReference#asText");
    return this.asLocalized().asText(sample);
  }

  @Override
  public @NonNull LocalizedReference format(@NonNull Object... objects) {
    this.objects.addAll(Arrays.asList(objects));
    this.extra.forEach(line -> line.format(objects));
    return this;
  }

  @Override
  public @NonNull LocalizedReference format(@NonNull Formatter formatter) {
    this.formatters.add(formatter);
    return this;
  }

  @Override
  public @NonNull LocalizedReference append(@NonNull Line line) {
    this.extra.add(line);
    return this;
  }

  @Override
  public @NonNull LocalizedReference format(@NonNull Placeholder placeholder) {
    this.linePlaceholder.add(placeholder);
    return this;
  }

  @Override
  public @NonNull String getRaw() {
    ErrorHandler.getInstance().handle(Level.FINEST, "Raw use of LocalizedReference#getRaw");
    return this.asLocalized().getRaw();
  }

  @Override
  public @NonNull LocalizedReference setRaw(@NonNull String raw) {
    this.key = raw;
    return this;
  }

  @Override
  public @NonNull LocalizedReference append(@NonNull String string) {
    return (LocalizedReference) Line.super.append(string);
  }
}
