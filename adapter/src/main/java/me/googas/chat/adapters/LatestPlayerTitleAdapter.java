package me.googas.chat.adapters;

import lombok.NonNull;
import org.bukkit.entity.Player;

public final class LatestPlayerTitleAdapter implements PlayerTitleAdapter {
  @Override
  public void sendTitle(
      @NonNull Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
  }
}
