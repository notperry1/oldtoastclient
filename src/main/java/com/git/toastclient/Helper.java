package com.git.toastclient;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Helper {

	public static String coords(Vec3d coords) {
		return String.format("<%2f,%2f,%2f>", coords.getX(), coords.getY(), coords.getZ());
	}

	public static String coords(Vec3i coords) {
		return String.format("<%d,%d,%d>", coords.getX(), coords.getY(), coords.getZ());
	}
	
}
