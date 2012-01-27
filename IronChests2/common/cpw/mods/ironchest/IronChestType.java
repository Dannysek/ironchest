package cpw.mods.ironchest;

import java.util.Arrays;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_IronChest;

public enum IronChestType {
	IRON(54, "IronChest", "ironchest.png", 0, Item.ingotIron, TileEntityIronChest.class, "mmmmPmmmm"),
	GOLD(81, "GoldChest", "goldchest.png", 1, Item.ingotGold, TileEntityGoldChest.class, "mmmmPmmmm");
	// DIAMOND(108,"DiamondChest","diamondchest.png",2);
	int size;
	String friendlyName;
	private String modelTexture;
	private int textureRow;
	private Class<? extends TileEntityIronChest> clazz;
	private Item mat;
	private String[] recipes;

	IronChestType(int size, String friendlyName, String modelTexture, int textureRow, Item mat, Class<? extends TileEntityIronChest> clazz, String... recipes) {
		this.size = size;
		this.friendlyName = friendlyName;
		this.modelTexture = modelTexture;
		this.textureRow = textureRow;
		this.clazz = clazz;
		this.mat = mat;
		this.recipes=recipes;
	}

	public String getModelTexture() {
		return modelTexture;
	}

	public int getTextureRow() {
		return textureRow;
	}

	public static TileEntity makeEntity(int metadata) {
		//Compatibility			
		int chesttype=metadata;
		int facing=0;
	
		if (metadata>2) {
			chesttype=metadata<<2;
			facing=metadata&3;
		}
		try {
			TileEntityIronChest te=values()[chesttype].clazz.newInstance();
			if (mod_IronChest.compatibilityMode) {
				te.setFacing((byte)facing);
			}
			return te;
		} catch (InstantiationException e) {
			// unpossible
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// unpossible
			e.printStackTrace();
		}
		return null;
	}

	public static void registerTileEntities() {
		for (IronChestType typ : values()) {
			System.out.printf("Registering %s\n",typ.friendlyName);
			ModLoader.RegisterTileEntity(typ.clazz, typ.friendlyName);
		}
	}

	public static void registerRecipes(BlockIronChest blockResult) {
		ItemStack previous=new ItemStack(Block.chest);
		for (IronChestType typ : values()) {
			for (String recipe : typ.recipes) {
				addRecipe(new ItemStack(blockResult, typ.ordinal()), recipe, 'm', typ.mat, 'P', previous, 'G', Block.glass, 'C', Block.chest);
			}
			previous=new ItemStack(blockResult,typ.ordinal());
		}
	}
	private static void addRecipe(ItemStack is, Object... parts) {
		System.out.printf("Recipe : %s\n",Arrays.asList(parts));
		ModLoader.AddRecipe(is, parts);
	}
}