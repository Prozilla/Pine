package dev.prozilla.pine.common.asset.pool;

public enum TextureArrayPolicy {
	/** Always load texture in a texture array. */
	ALWAYS {
		@Override
		public boolean canCreateArray() {
			return true;
		}
		
		@Override
		public boolean canUseArray() {
			return true;
		}
	},
	/** Only load texture in a texture array if there is one available. */
	SOMETIMES {
		@Override
		public boolean canCreateArray() {
			return false;
		}
		
		@Override
		public boolean canUseArray() {
			return true;
		}
	},
	/** Never load texture in a texture array. */
	NEVER {
		@Override
		public boolean canCreateArray() {
			return false;
		}
		
		@Override
		public boolean canUseArray() {
			return false;
		}
	};
	
	public abstract boolean canCreateArray();
	public abstract boolean canUseArray();
}
