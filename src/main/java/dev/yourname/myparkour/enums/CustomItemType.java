package dev.yourname.myparkour.enums;

public enum CustomItemType {
	RESET_TO_CHECKPOINT,
	EXIT_PARKOUR;

	public String getId() {
		return this.name().toLowerCase();
	}
}
