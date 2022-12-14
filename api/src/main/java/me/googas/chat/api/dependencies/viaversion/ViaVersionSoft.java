package me.googas.chat.api.dependencies.viaversion;

import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.api.PlayerChannel;
import me.googas.chat.api.util.Versions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class ViaVersionSoft {

  @Getter private static boolean enabled;
  private static ViaVersionProtocol protocol;

  static {
    reload();
  }

  private static void reload() {
    enabled = Bukkit.getServer().getPluginManager().getPlugin("ViaVersion") != null;
    if (enabled) {
      protocol = new ViaVersionProtocol();
    } else {
      protocol = null;
    }
  }

  @NonNull
  public static ViaVersionProtocol getProtocol() {
    return Objects.requireNonNull(
        protocol, "Protocol has not been initialized. Check if it has been using #isEnabled");
  }

  @NonNull
  public static void registerProtocol(@NonNull Plugin plugin) {
    Bukkit.getServer().getPluginManager().registerEvents(getProtocol(), plugin);
  }

  @NonNull
  public static Versions.Player getVersion(@NonNull Player player) {
    return getProtocol().getVersion(player);
  }

  @NonNull
  public static PlayerChannel getProtocolChannel(@NonNull UUID uniqueId) {
    return getProtocol().getChannel(uniqueId);
  }
}
