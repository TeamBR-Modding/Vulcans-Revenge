package com.pauljoda.vulcansrevenge.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.pauljoda.vulcansrevenge.api.sword.SwordMode;
import com.pauljoda.vulcansrevenge.common.sword.ItemVulcanSword;
import com.pauljoda.vulcansrevenge.lib.Reference;
import com.teambr.nucleus.client.ModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * This file was created for VulcansRevenge
 * <p>
 * VulcansRevenge is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/28/17
 */
public class ModelVulcanSword implements IModel {

    public ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "items/sword/normal");

    public static final ModelResourceLocation LOCATION =
            new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "item_vulcan_sword"), "inventory");

    public static final IModel MODEL = new ModelVulcanSword();
    
    public static final Map<String, IBakedModel> cache = Maps.<String, IBakedModel>newHashMap(); // contains all the baked models since they'll never change

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format,
                            Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);
        TRSRTransformation transform = state.apply(java.util.Optional.empty()).orElse(TRSRTransformation.identity());
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        IBakedModel model = (new ItemLayerModel(ImmutableList.of(texture))).bake(state, format, bakedTextureGetter);
        builder.addAll(model.getQuads(null, null, 0));

        return new BakedVulcanSwordModel(this, builder.build(),
                bakedTextureGetter.apply(texture), format, Maps.immutableEnumMap(transformMap), Maps.<String, IBakedModel>newHashMap());
    }

    // Loader
    public enum LoaderVulcanSword implements ICustomModelLoader {
        INSTANCE;

        @Override
        public boolean accepts(ResourceLocation modelLocation) {
            return modelLocation.getResourceDomain().equals(Reference.MOD_ID) &&
                    modelLocation.getResourcePath().equals(LOCATION.getResourcePath());
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) throws Exception {
            return MODEL;
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {
            // no need to clear cache since we create a new model instance
        }
    }

    // Override
    private static final class VulcanSwordOverrideHandler extends ItemOverrideList {
        public static final VulcanSwordOverrideHandler INSTANCE = new VulcanSwordOverrideHandler();

        public VulcanSwordOverrideHandler() {
            super(ImmutableList.<ItemOverride>of());
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
            String modelName = stack.hasTagCompound() ? stack.getTagCompound().getString(ItemVulcanSword.NBT_MODE) : "vulcansrevenge:normal";

            BakedVulcanSwordModel model = (BakedVulcanSwordModel) originalModel;

            if(!cache.containsKey(modelName)) {
                model.parent.texture = GameRegistry.findRegistry(SwordMode.class).containsKey(new ResourceLocation(modelName)) ? // Is valid?
                        GameRegistry.findRegistry(SwordMode.class).getValue(new ResourceLocation(modelName)).getTexturePath() :  // Use if valid
                        GameRegistry.findRegistry(SwordMode.class).getValue(new ResourceLocation("vulcansrevenge:normal")).getTexturePath(); // Default to normal if not
                
                Function<ResourceLocation, TextureAtlasSprite> textureGetter;
                textureGetter = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                IBakedModel bakedModel = model.parent.bake(new SimpleModelState(model.transforms), model.format, textureGetter);
                cache.put(modelName, bakedModel);
                return bakedModel;
            }

            return cache.get(modelName);
        }
    }

    private static final class BakedVulcanSwordModel extends BakedItemModel {

        private final ModelVulcanSword parent;
        private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change
        private final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
        private final ImmutableList<BakedQuad> quads;
        private final TextureAtlasSprite particle;
        private final VertexFormat format;

        public BakedVulcanSwordModel(ModelVulcanSword parent,
                               ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, VertexFormat format,
                               ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms,
                               Map<String, IBakedModel> cache) {
            super(quads, particle, transforms, VulcanSwordOverrideHandler.INSTANCE);
            this.quads = quads;
            this.particle = particle;
            this.format = format;
            this.parent = parent;
            this.transforms = transforms;
            this.cache = cache;
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
            // Wrap the base and have it handle the movement
            return PerspectiveMapWrapper
                    .handlePerspective(
                            this,
                            ModelHelper.DEFAULT_TOOL_STATE,
                            cameraTransformType);
        }
    }
}
