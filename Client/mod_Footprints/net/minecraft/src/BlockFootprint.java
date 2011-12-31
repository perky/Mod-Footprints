package net.minecraft.src;

import java.util.Random;

public class BlockFootprint extends Block {
	
	public static int[] footprintTextures = {
		ModLoader.addOverride("/terrain.png", "/item/footprint_human.png"),
		ModLoader.addOverride("/terrain.png", "/item/footprint_cow.png"),
		ModLoader.addOverride("/terrain.png", "/item/footprint_pig.png"),
		ModLoader.addOverride("/terrain.png", "/item/footprint_chicken.png"),
		ModLoader.addOverride("/terrain.png", "/item/footprint_creeper.png"),
		ModLoader.addOverride("/terrain.png", "/item/footprint_skeleton.png")
	};

	public BlockFootprint(int i)
	{
		super(i, 0, Material.ice);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.025F, 1.0F);
        blockIndexInTexture = footprintTextures[0];
	}
	
	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        super.onNeighborBlockChange(world, i, j, k, l);
        int blockid = world.getBlockId(i, j+1, k);
        int blockmd = world.getBlockMetadata(i, j+1, k);
        if(blockid != 0)
        {
        	world.setBlockWithNotify(i, j+1, k, 0);
        	world.setBlockAndMetadataWithNotify(i, j, k, blockid, blockmd);
        }
        if(world.getBlockId(i, j-1, k) == 0)
        {
        	world.setBlockWithNotify(i, j, k, 0);
        }
        
        checkForWater(world, i, j, k);
        
    }
	
	private void checkForWater(World world, int i, int j, int k)
	{
		if(Block.blocksList[world.getBlockId(i-1, j, k)] instanceof BlockFluid)
        	world.setBlockWithNotify(i, j, k, 0);
        if(Block.blocksList[world.getBlockId(i+1, j, k)] instanceof BlockFluid)
        	world.setBlockWithNotify(i, j, k, 0);
        if(Block.blocksList[world.getBlockId(i, j, k-1)] instanceof BlockFluid)
        	world.setBlockWithNotify(i, j, k, 0);
        if(Block.blocksList[world.getBlockId(i, j, k+1)] instanceof BlockFluid)
        	world.setBlockWithNotify(i, j, k, 0);
        if(Block.blocksList[world.getBlockId(i, j+1, k)] instanceof BlockFluid)
        	world.setBlockWithNotify(i, j, k, 0);
	}
	
	public int tickRate()
	{
		return 1;
	}
	
	public void onBlockAdded(World world, int i, int j, int k)
	{
		if(mod_Footprints.rainWashesFootprints)
		{
			world.scheduleBlockUpdate(i, j, k, this.blockID, 200);
		} else
		if(mod_Footprints.footprintDieAfterTicks != 0)
		{
			world.scheduleBlockUpdate(i, j, k, this.blockID, mod_Footprints.footprintDieAfterTicks);
		}
		
		checkForWater(world, i, j, k);
	}
	
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		if(!world.multiplayerWorld)
		{
			if(mod_Footprints.rainWashesFootprints)
			{
				world.scheduleBlockUpdate(i, j, k, this.blockID, 200);
				if(world.isRaining() && world.canBlockSeeTheSky(i, j, k))
					world.setBlockWithNotify(i, j, k, 0);
			} else {
				if(world.getBlockMetadata(i, j, k) == 4)
					world.setBlockWithNotify(i, j, k, Block.snow.blockID);
				else
					world.setBlockWithNotify(i, j, k, 0);
			}
		}
	}
	
	public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
		if(j<6)
		{
			return footprintTextures[j];
		}
		else
		{
			return Block.snow.blockIndexInTexture;
		}
    }
	
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        float f = 0.001F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }
	
	public boolean isOpaqueCube()
	{
	    return false;
	}
	
	public boolean renderAsNormalBlock()
	{
	    return false;
	}
	
	public int getRenderBlockPass()
    {
        return 1;
    }

    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return super.shouldSideBeRendered(iblockaccess, i, j, k, 1 - l);
    }
	
}
