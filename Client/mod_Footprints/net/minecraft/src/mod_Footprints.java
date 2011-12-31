package net.minecraft.src;

import java.util.List;

import net.minecraft.client.Minecraft;

public class mod_Footprints extends BaseModMp {
	@MLProp public static String configinstructions = "Set rainWashesFootprints to false if you are changing footprintDieAfterTicks > 0.";
	@MLProp public static int blockFootprintBlockId = 220;
	@MLProp public static boolean rainWashesFootprints = true;
	@MLProp public static int footprintDieAfterTicks = 0;
	@MLProp public static boolean mobsLeaveFootprints = true;
	@MLProp public static boolean playersLeaveFootprints = true;
	@MLProp public static boolean canTrampleOnFoliage = true;
	@MLProp public static boolean noFootprintsOnSneak = true;
	
	public static Block blockFootprint = new BlockFootprint(blockFootprintBlockId).setBlockName("footprint");
	public float footprintWait = 0;
	
	private static Block immutableblocks[] = {
		Block.torchRedstoneActive,
		Block.torchRedstoneIdle,
		Block.torchWood,
		Block.redstoneWire,
		Block.redstoneRepeaterActive,
		Block.redstoneRepeaterIdle,
		Block.fence,
		Block.fenceGate,
		Block.fenceIron,
		Block.glass,
	};
	
	private static Block optionalImmutableblocks[] = {
		Block.crops,
		Block.sapling,
		Block.plantRed,
		Block.plantYellow
	};
			
	public mod_Footprints()
	{
		ModLoader.RegisterBlock(blockFootprint);
		ModLoader.AddName(blockFootprint, "Footprint");
		ModLoader.SetInGameHook(this, true, true);
	}
	
	public boolean OnTickInGame(float f, Minecraft minecraft)
    {
		footprintWait += f;
		if(footprintWait > 1.5)
		{
			footprintWait = 0;
			World world = minecraft.theWorld;
			int l,j,k;
			
			List entities = world.getLoadedEntityList();
			for(int i = 0; i < entities.size(); i++)
			{
				Entity entity = (Entity)entities.get(i);
				l = MathHelper.floor_double(entity.posX);
				j = MathHelper.floor_double(entity.posY);
				k = MathHelper.floor_double(entity.posZ);
				
				if(playersLeaveFootprints && entity instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer)entity;
					if(noFootprintsOnSneak && !player.isSneaking())
						addFootprint(world,l,j-1,k,0);
					else
						addFootprint(world,l,j-1,k,0);
				} else
				if(mobsLeaveFootprints && entity instanceof EntityZombie)
				{
					addFootprint(world,l,j,k,0);
				}
				if(mobsLeaveFootprints && entity instanceof EntityCow)
				{
					addFootprint(world,l,j,k,1);
				} else
				if(mobsLeaveFootprints && (entity instanceof EntityPig || entity instanceof EntitySheep))
				{
					addFootprint(world,l,j,k,2);
				} else
				if(mobsLeaveFootprints && entity instanceof EntityChicken)
				{
					addFootprint(world,l,j,k,3);
				} else
				if(mobsLeaveFootprints && entity instanceof EntityCreeper)
				{
					addFootprint(world,l,j,k,4);
				} else
				if(mobsLeaveFootprints && entity instanceof EntitySkeleton)
				{
					addFootprint(world,l,j,k,5);
				}
			}
		}
		
        return true;
    }
	
	public void addFootprint(World world, int l, int j, int k, int m)
	{
		int feetId = world.getBlockId(l, j, k);
		int floorId = world.getBlockId(l, j-1, k);
		
		for(int i = 0; i < immutableblocks.length; i++)
		{
			if(feetId == immutableblocks[i].blockID)
				return;
		}
		
		if(!canTrampleOnFoliage)
		{
			for(int i = 0; i < optionalImmutableblocks.length; i++)
			{
				if(feetId == optionalImmutableblocks[i].blockID)
					return;
			}
		}
		
		if(feetId == Block.snow.blockID)
		{
			world.setBlockAndMetadataWithNotify(l, j, k, blockFootprint.blockID, 5);
		}
		else
		if(floorId != 0 && Block.snow.canPlaceBlockAt(world, l, j, k)
			&& feetId != Block.waterMoving.blockID && feetId != Block.waterStill.blockID)
		{
			if(floorId == Block.sand.blockID || floorId == Block.dirt.blockID || floorId == Block.slowSand.blockID || floorId == Block.gravel.blockID
					|| floorId == Block.grass.blockID)
			world.setBlockAndMetadataWithNotify(l, j, k, blockFootprint.blockID, m);
		}
	}
	
	public void load()
	{
	}
	
	public String getVersion()
	{
		return "1.4";
	}
}
