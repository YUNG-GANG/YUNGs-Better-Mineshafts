package com.yungnickyoung.minecraft.bettermineshafts.mixin;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.server.command.LocateCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.structure.MineshaftGenerator;
import net.minecraft.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.server.command.CommandManager.literal;

@Mixin(LocateCommand.class)
public abstract class LocateMixin {
    private LocateMixin() {
        // NO-OP
    }

    @Shadow
    private static int execute(ServerCommandSource source, String name) {
        throw new AssertionError();
    }

    //	@Inject(at = @At("HEAD"), method = "init()V")
//	private void init(CallbackInfo info) {
//		System.out.println("This line is printed by an example mod mixin!");
//	}
    @Inject(method = "register", at = @At(value = "RETURN"))
    private static void onRegister(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo info) {
        dispatcher.register(literal("locate").requires(source -> source.hasPermissionLevel(2))
                .then(literal("BetterMineshaft").executes(ctx -> execute(ctx.getSource(), "Better_Mineshaft"))));
    }
}
