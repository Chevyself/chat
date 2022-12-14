package me.googas.chat.packet.chat;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import lombok.Getter;
import lombok.NonNull;
import me.googas.chat.packet.Packet;
import me.googas.chat.packet.PacketDataWrapper;
import me.googas.reflect.SimpleWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/** Wraps the 'IChatBaseComponent' nms class. */
public final class WrappedChatComponent extends SimpleWrapper<Object> implements PacketDataWrapper {

  @NonNull
  private static final WrappedClass<?> CHAT_BASE_COMPONENT =
      WrappedClass.forName("net.minecraft.server." + Packet.NMS + ".IChatBaseComponent");

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  WrappedChatComponent(Object reference) {
    super(reference);
  }

  /**
   * Wrap components into a chat component.
   *
   * @param components the components to wrap
   * @return the wrapped components
   */
  @NonNull
  public static WrappedChatComponent of(@NonNull BaseComponent[] components) {
    return WrappedChatComponent.of(components, WrappedChatComponent.CHAT_BASE_COMPONENT.getClazz());
  }

  /**
   * Wrap chat components from the given {@link Type} of chat component.
   *
   * @param components the components to wrap
   * @param typeOfComponent the type of the chat component
   * @return the wrapped components
   */
  @NonNull
  public static WrappedChatComponent of(
      @NonNull BaseComponent[] components, @NonNull Type typeOfComponent) {
    return new WrappedChatComponent(
        Serializer.gson.fromJson(ComponentSerializer.toString(components), typeOfComponent));
  }

  @Override
  public Object getHandle() {
    return this.reference;
  }

  /** Wrapper for the 'IChatBaseComponent$ChatSerializer'. */
  public static class Serializer {

    @NonNull
    private static final WrappedClass<?> CHAT_SERIALIZER =
        WrappedClass.forName(
            "net.minecraft.server." + Packet.NMS + ".IChatBaseComponent$ChatSerializer");

    @NonNull @Getter private static final Gson gson = Serializer.getChatSerializer();

    @NonNull
    private static Gson getChatSerializer() {
      try {
        return Serializer.CHAT_SERIALIZER.getDeclaredField(Gson.class, "a").get(null);
      } catch (IllegalAccessException e) {
        throw new IllegalStateException("Could not get Gson from field");
      }
    }
  }
}
