/**
 * Copyright 2022 Markus Bordihn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.markusbordihn.dailyrewards.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.server.MinecraftServer;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import de.markusbordihn.dailyrewards.Constants;

@EventBusSubscriber
public class CommandManager {

  protected CommandManager() {}

  protected static final Logger log = LogManager.getLogger(Constants.LOG_NAME);

  @SubscribeEvent
  public static void handleRegisterCommandsEvent(RegisterCommandsEvent event) {
    log.info("Registering {} commands ...", Constants.MOD_COMMAND);
    CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();
    commandDispatcher.register(Commands.literal(Constants.MOD_COMMAND)
    // @formatter:off
        .then(ClaimCommand.register())
        .then(PreviewCommand.register())
      // @formatter:on
    );
  }

  public static void executeServerCommand(String command) {
    MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();
    if (minecraftServer == null) {
      return;
    }
    log.debug("Execute Server Command: {}", command);
    Commands commands = minecraftServer.getCommands();
    CommandSource commandSourceStack =
        minecraftServer.createCommandSourceStack().withSuppressedOutput();
    commands.performCommand(commandSourceStack, command);
  }

  public static void executeUserCommand(String command) {
    MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();
    if (minecraftServer == null) {
      return;
    }
    log.debug("Execute User Command: {}", command);
    Commands commands = minecraftServer.getCommands();
    CommandSource commandSourceStack = minecraftServer.createCommandSourceStack();
    commands.performCommand(commandSourceStack, command);
  }
}
