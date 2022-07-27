package me.googas.chat.api.lines;

import java.util.*;
import java.util.logging.Level;
import lombok.NonNull;
import me.googas.chat.ErrorHandler;
import me.googas.chat.api.Channel;
import me.googas.chat.api.Language;
import me.googas.chat.api.lines.format.Formatter;
import me.googas.commands.bukkit.result.Result;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/** A {@link Line} that references a language key to be built into {@link Localized}. */
public final class LocalizedReference implements Line {

  /** Objects formatters. */
  @NonNull private final List<Object> objects;
  /** Placeholders formatters. */
  @NonNull private final Map<String, String> placeholders;
  /** Formatters. */
  @NonNull private final List<me.googas.chat.api.lines.format.Formatter> formatters;

  @NonNull private String key;

  LocalizedReference(@NonNull String key) {
    this(new ArrayList<>(), new HashMap<>(), new ArrayList<>(), key);
  }

  private LocalizedReference(
      @NonNull List<Object> objects,
      @NonNull Map<String, String> placeholders,
      @NonNull List<me.googas.chat.api.lines.format.Formatter> formatters,
      @NonNull String key) {
    this.objects = objects;
    this.placeholders = placeholders;
    this.formatters = formatters;
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
    if (!objects.isEmpty()) localized.format(objects.toArray());
    if (!placeholders.isEmpty()) localized.format(placeholders);
    if (!formatters.isEmpty()) localized.format(formatters);
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
    return this.asLocalized(channel.getLocale().orElse(Locale.ENGLISH));
  }

  /**
   * Raw use of {@link Localized}. This will warn the {@link java.util.logging.Logger} when used
   *
   * @return the {@link Localized}
   */
  public @NonNull Localized asLocalized() {
    return this.asLocalized(Locale.ENGLISH);
  }

  @Override
  public @NonNull LocalizedReference copy() {
    return new LocalizedReference(
        new ArrayList<>(this.objects),
        new HashMap<>(this.placeholders),
        new ArrayList<>(this.formatters),
        this.key);
  }

  @Override
  public @NonNull Result asResult() {
    ErrorHandler.getInstance().handle(Level.WARNING, "Raw use of LocalizedReference#asResult");
    return this.asLocalized().asResult();
  }

  @Override
  public @NonNull BaseComponent[] build() {
    ErrorHandler.getInstance().handle(Level.WARNING, "Raw use of LocalizedReference#build");
    return this.asLocalized().build();
  }

  @Override
  public @NonNull Optional<String> asText() {
    ErrorHandler.getInstance().handle(Level.WARNING, "Raw use of LocalizedReference#asText");
    return this.asLocalized().asText();
  }

  @Override
  public @NonNull LocalizedReference format(@NonNull Object... objects) {
    this.objects.addAll(Arrays.asList(objects));
    return this;
  }

  @Override
  public @NonNull LocalizedReference format(@NonNull Map<String, String> map) {
    this.placeholders.putAll(map);
    return this;
  }

  @Override
  public @NonNull LocalizedReference format(@NonNull Formatter formatter) {
    this.formatters.add(formatter);
    return this;
  }

  @Override
  public @NonNull String getRaw() {
    ErrorHandler.getInstance().handle(Level.WARNING, "Raw use of LocalizedReference#getRaw");
    return this.asLocalized().getRaw();
  }

  @Override
  public @NonNull LocalizedReference setRaw(@NonNull String raw) {
    this.key = raw;
    return this;
  }

  @Override
  public BaseComponent[] buildWithPlaceholders(@NonNull OfflinePlayer player) {
    Player onlinePlayer = player.getPlayer();
    return this.asLocalized(
            onlinePlayer == null ? Locale.ENGLISH : Language.getLocale(onlinePlayer))
        .buildWithPlaceholders(player);
  }

  @Override
  public @NonNull Optional<String> asTextWithPlaceholders(@NonNull OfflinePlayer player) {
    Player onlinePlayer = player.getPlayer();
    return this.asLocalized(
            onlinePlayer == null ? Locale.ENGLISH : Language.getLocale(onlinePlayer))
        .asTextWithPlaceholders(player);
  }
}