package com.silverminer.shrines.structures.nether_tempel;

import java.util.List;
import java.util.Random;

import com.silverminer.shrines.loot_tables.ShrinesLootTables;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class NetherTempelPiece {
	private static final ResourceLocation location = new ResourceLocation("shrines:nether_tempel/nether_tempel");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new NetherTempelPiece.Piece(templateManager, location, pos.add(0, -1, 0), rotation, 0));
	}

	public static class Piece extends TemplateStructurePiece {
		private final ResourceLocation location;
		private final Rotation rotation;

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(IStructurePieceType.register(NetherTempelPiece.Piece::new, "nether_tempel"), componentTypeIn);
			this.location = location;
			this.templatePosition = pos;
			this.rotation = rotation;
			this.setup(templateManager);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(IStructurePieceType.register(NetherTempelPiece.Piece::new, "nether_tempel"), cNBT);
			this.location = new ResourceLocation(cNBT.getString("Template"));
			this.rotation = Rotation.valueOf(cNBT.getString("Rot"));
			this.setup(templateManager);
		}

		private void setup(TemplateManager templateManager) {
			Template template = templateManager.getTemplateDefaulted(this.location);
			PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation)
					.setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
			this.setup(template, this.templatePosition, placementsettings);
		}

		/**
		 * (abstract) Helper method to read subclass data from NBT
		 */
		protected void readAdditional(CompoundNBT tagCompound) {
			super.readAdditional(tagCompound);
			tagCompound.putString("Template", this.location.toString());
			tagCompound.putString("Rot", this.rotation.name());
		}

		protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			if ("chest_left".equals(function) || "chest_right".equals(function) || "chest_d1".equals(function)
					|| "chest_d2".equals(function) || "chest_d3".equals(function) || "chest_d4".equals(function)) {
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
				TileEntity tileentity = worldIn.getTileEntity(pos.down());
				if (tileentity instanceof ChestTileEntity) {
					((ChestTileEntity) tileentity).setLootTable(ShrinesLootTables.getRandomNetherLoot(rand),
							rand.nextLong());
				}

			}
		}

		public boolean func_230383_a_(ISeedReader p_230383_1_, StructureManager p_230383_2_, ChunkGenerator p_230383_3_,
				Random p_230383_4_, MutableBoundingBox p_230383_5_, ChunkPos p_230383_6_, BlockPos p_230383_7_) {
			PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation)
					.setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
			BlockPos blockpos1 = this.templatePosition
					.add(Template.transformedBlockPos(placementsettings, new BlockPos(3, 0, 0)));
			int i = p_230383_1_.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos1.getX(), blockpos1.getZ());
			BlockPos blockpos2 = this.templatePosition;
			this.templatePosition = this.templatePosition.add(0, i - 90 - 1, 0);
			boolean flag = super.func_230383_a_(p_230383_1_, p_230383_2_, p_230383_3_, p_230383_4_, p_230383_5_,
					p_230383_6_, p_230383_7_);

			this.templatePosition = blockpos2;
			return flag;
		}
	}
}