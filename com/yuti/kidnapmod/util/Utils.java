package com.yuti.kidnapmod.util;

import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.data.UsersSettingsData;
import com.yuti.kidnapmod.init.KidnapModSoundEvents;
import com.yuti.kidnapmod.items.IHasResistance;
import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.items.ItemBlindfold;
import com.yuti.kidnapmod.items.ItemClothes;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.items.ItemEarplugs;
import com.yuti.kidnapmod.items.ItemGag;
import com.yuti.kidnapmod.items.ItemUsuableOnRestrainedPlayer;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.tileentity.ITileEntityBondageItemHolder;
import com.yuti.kidnapmod.util.teleport.Position;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils {
   private static final UUID setSpeedUUID = UUID.fromString("015ad548-47e6-11e8-842f-0ed5f89f718b");
   private static Logger logger;
   private static String femalesNamesFileRessourcePath = "/assets/knapm/names/kidnapper_female_names.txt";
   private static List<String> kidnappersNames = loadNames();
   public static Biome[] biomesForKidnappers;

   public static void changePlayerSpeed(EntityPlayer player, double speed) {
      int operations = 0;
      String name = "speed_change";
      IAttributeInstance movement = player.func_110148_a(SharedMonsterAttributes.field_111263_d);
      AttributeModifier setSpeedBonus = new AttributeModifier(setSpeedUUID, name, speed, operations);
      if (movement.func_111127_a(setSpeedUUID) != null) {
         movement.func_188479_b(setSpeedUUID);
      }

      movement.func_111121_a(setSpeedBonus);
   }

   public static boolean inArmorSlot(EntityEquipmentSlot slot) {
      return slot == EntityEquipmentSlot.CHEST || slot == EntityEquipmentSlot.HEAD || slot == EntityEquipmentSlot.FEET || slot == EntityEquipmentSlot.LEGS;
   }

   public static Logger getLogger() {
      if (logger == null) {
         logger = LogManager.getFormatterLogger("knapm");
      }

      return logger;
   }

   private static List<String> loadNames() {
      ArrayList names = new ArrayList();

      try {
         InputStream stream = KidnapModMain.class.getResourceAsStream(femalesNamesFileRessourcePath);
         InputStreamReader reader = new InputStreamReader(stream);
         BufferedReader br = new BufferedReader(reader);

         String ligne;
         while((ligne = br.readLine()) != null) {
            String name = ligne.replace(" ", "");
            names.add(name);
         }
      } catch (IOException var6) {
         var6.printStackTrace();
      }

      return names;
   }

   public static String getRandomName() {
      if (kidnappersNames.size() == 0) {
         return "No name defined";
      } else {
         Random rand = new Random();
         int nameIndex = rand.nextInt(kidnappersNames.size());
         return (String)kidnappersNames.get(nameIndex);
      }
   }

   public static boolean isSpecialBindItem(Item item) {
      return item instanceof ItemUsuableOnRestrainedPlayer || item instanceof ItemFood || item instanceof ItemPotion || item instanceof ItemBucketMilk;
   }

   public static boolean isOpe(EntityPlayer player) {
      return FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_152603_m().func_152683_b(player.func_146103_bH()) != null;
   }

   public static void sendMessageToPlayersInArea(EntityPlayer sender, double distance, ITextComponent message) {
      sendMessageToPlayersInArea(sender, distance, message, false);
   }

   public static void sendMessageToPlayers(EntityPlayer sender, ITextComponent message) {
      sendMessageToPlayers(sender, message, false);
   }

   public static void sendMessageToPlayersInArea(EntityPlayer sender, double distance, ITextComponent message, boolean bypass) {
      List<EntityPlayerMP> players = PlayerBindState.getPlayerAround(sender.field_70170_p, sender.func_180425_c(), distance);
      Iterator var6 = players.iterator();

      while(var6.hasNext()) {
         EntityPlayer player = (EntityPlayer)var6.next();
         sendMessageFrom(player, sender, message, bypass);
      }

      sendMessageToServer(message);
   }

   public static void sendMessageToPlayers(EntityPlayer sender, ITextComponent message, boolean bypass) {
      if (sender != null && sender.field_70170_p != null) {
         List<EntityPlayerMP> players = PlayerBindState.getPlayers(sender.field_70170_p);
         Iterator var4 = players.iterator();

         while(var4.hasNext()) {
            EntityPlayer player = (EntityPlayer)var4.next();
            sendMessageFrom(player, sender, message, bypass);
         }

         sendMessageToServer(message);
      }
   }

   public static boolean isPlayerInRadiusFrom(EntityPlayer receiver, EntityPlayer sender, int radius) {
      List<EntityPlayerMP> players = PlayerBindState.getPlayerAround(receiver.field_70170_p, receiver.func_180425_c(), (double)radius);
      Iterator var4 = players.iterator();

      EntityPlayer player;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         player = (EntityPlayer)var4.next();
      } while(!player.equals(sender));

      return true;
   }

   public static void sendMessageFrom(EntityPlayer receiver, EntityPlayer sender, ITextComponent message, boolean bypass) {
      if (receiver != null && receiver.field_70170_p != null && sender != null) {
         PlayerBindState state = PlayerBindState.getInstance(receiver);
         if (state != null) {
            if (bypass || !state.hasEarplugs()) {
               UsersSettingsData settings = UsersSettingsData.get(receiver.field_70170_p);
               if (settings != null) {
                  if (!bypass && settings.checkBlocked(receiver, sender)) {
                     return;
                  }

                  int area = settings.getTalkArea(receiver);
                  if (area >= 0) {
                     if (!bypass && !isPlayerInRadiusFrom(receiver, sender, area)) {
                        return;
                     }

                     ITextComponent newMessage = new TextComponentString("*");
                     newMessage.func_150256_b().func_150238_a(TextFormatting.RED);
                     ITextComponent baseMessage = message.func_150259_f();
                     if (baseMessage.func_150256_b().func_150215_a() == null) {
                        baseMessage.func_150256_b().func_150238_a(TextFormatting.WHITE);
                     }

                     ITextComponent newMessage = newMessage.func_150257_a(baseMessage);
                     receiver.func_145747_a(newMessage);
                     return;
                  }
               }

               receiver.func_145747_a(message);
            }
         }
      }
   }

   public static void sendMessageToServer(ITextComponent message) {
      FMLCommonHandler.instance().getMinecraftServerInstance().func_145747_a(message);
   }

   public static void sendMessageToServer(String message) {
      sendMessageToServer((ITextComponent)(new TextComponentString(message)));
   }

   public static String getMessageFromArray(String[] array, int indexStar, int indexEnd) {
      String msg = "";
      if (indexEnd <= array.length && indexStar >= 0) {
         for(int i = indexStar; i < indexEnd; ++i) {
            msg = msg + array[i] + " ";
         }
      }

      if (msg.length() > 0) {
         msg = msg.substring(0, msg.length() - 1);
      }

      return msg;
   }

   public static byte[] serialize(Object obj) throws IOException {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(out);
      os.writeObject(obj);
      return out.toByteArray();
   }

   public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
      ByteArrayInputStream in = new ByteArrayInputStream(data);
      ObjectInputStream is = new ObjectInputStream(in);
      return is.readObject();
   }

   public static void sendErrorMessageToEntity(ICommandSender entity, String message) {
      TextComponentString comp = new TextComponentString(message);
      comp.func_150256_b().func_150238_a(TextFormatting.RED);
      entity.func_145747_a(comp);
   }

   public static void sendValidMessageToEntity(ICommandSender entity, String message) {
      TextComponentString comp = new TextComponentString(message);
      comp.func_150256_b().func_150238_a(TextFormatting.GREEN);
      entity.func_145747_a(comp);
   }

   public static void sendInfoMessageToEntity(ICommandSender entity, String message) {
      TextComponentString comp = new TextComponentString(message);
      comp.func_150256_b().func_150238_a(TextFormatting.YELLOW);
      entity.func_145747_a(comp);
   }

   public static void sendMessageToEntity(ICommandSender entity, String message) {
      TextComponentString comp = new TextComponentString(message);
      entity.func_145747_a(comp);
   }

   public static void broadcastMessage(String message) {
      TextComponentString comp = new TextComponentString(message);
      FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_148539_a(comp);
   }

   @Nullable
   public static EntityPlayer getPlayerFromName(String name, World world) {
      if (world == null) {
         return null;
      } else {
         List<EntityPlayerMP> players = PlayerBindState.getPlayers(world);
         if (players != null) {
            Iterator var3 = players.iterator();

            while(var3.hasNext()) {
               EntityPlayer player = (EntityPlayer)var3.next();
               if (player.func_70005_c_().equals(name)) {
                  return player;
               }
            }
         }

         return null;
      }
   }

   public static NBTTagCompound getTagComponent(ItemStack stack) {
      NBTTagCompound nbt;
      if (stack.func_77942_o()) {
         nbt = stack.func_77978_p();
      } else {
         nbt = new NBTTagCompound();
      }

      return nbt;
   }

   public static void shockEntity(I_Kidnapped entity, EntityPlayer triggerer) {
      if (entity != null) {
         entity.shockKidnapped();
         sendValidMessageToEntity(triggerer, entity.getKidnappedName() + " has been shocked!");
      }

   }

   public static ItemStack extractValidStack(ItemStack stack) {
      if (stack == null) {
         return null;
      } else {
         ItemStack stackToExtract = stack.func_77946_l();
         if (!stackToExtract.func_190926_b()) {
            stackToExtract = stackToExtract.func_77979_a(1);
         }

         return stackToExtract;
      }
   }

   public static NBTTagCompound removeCoordinates(NBTTagCompound nbt) {
      if (nbt != null) {
         if (nbt.func_74764_b("x")) {
            nbt.func_82580_o("x");
         }

         if (nbt.func_74764_b("y")) {
            nbt.func_82580_o("y");
         }

         if (nbt.func_74764_b("z")) {
            nbt.func_82580_o("z");
         }
      }

      return nbt;
   }

   public static void setBondageElement(ItemStack stack, ItemStack bondageStack, String key, Class<?> origin, Class<?> cls) {
      if (stack != null && origin.isInstance(stack.func_77973_b()) && bondageStack != null && !bondageStack.func_190926_b() && cls.isInstance(bondageStack.func_77973_b())) {
         NBTTagCompound nbt = getTagComponent(stack);
         if (nbt != null) {
            NBTTagCompound tag = new NBTTagCompound();
            nbt.func_74782_a(key, bondageStack.func_77955_b(tag));
            stack.func_77982_d(nbt);
         }
      }

   }

   public static ItemStack getBondageElement(ItemStack stack, String key, Class<?> origin, Class<?> cls) {
      if (stack != null && origin.isInstance(stack.func_77973_b())) {
         NBTTagCompound nbt = getTagComponent(stack);
         if (nbt != null && nbt.func_74764_b(key)) {
            NBTBase tag = nbt.func_74781_a(key);
            if (tag instanceof NBTTagCompound) {
               NBTTagCompound tagBindCompound = (NBTTagCompound)tag;
               ItemStack stackReturned = new ItemStack(tagBindCompound);
               if (stackReturned != null && cls.isInstance(stackReturned.func_77973_b())) {
                  return stackReturned;
               }
            }
         }
      }

      return null;
   }

   public static void loadItemStack(World worldIn, BlockPos pos, ITileEntityBondageItemHolder tileEntity, ItemStack heldStack, ItemStack stack, @Nullable EntityPlayer loader) {
      if (stack != null && heldStack != null && tileEntity != null) {
         boolean loaded = false;
         if (stack.func_77973_b() instanceof ItemBind) {
            if (tileEntity.getBind() == null) {
               tileEntity.setBind(stack.func_77946_l());
               heldStack.func_190918_g(1);
               loaded = true;
            }
         } else if (stack.func_77973_b() instanceof ItemGag) {
            if (tileEntity.getGag() == null) {
               tileEntity.setGag(stack.func_77946_l());
               heldStack.func_190918_g(1);
               loaded = true;
            }
         } else if (stack.func_77973_b() instanceof ItemBlindfold) {
            if (tileEntity.getBlindfold() == null) {
               tileEntity.setBlindfold(stack.func_77946_l());
               heldStack.func_190918_g(1);
               loaded = true;
            }
         } else if (stack.func_77973_b() instanceof ItemEarplugs) {
            if (tileEntity.getEarplugs() == null) {
               tileEntity.setEarplugs(stack.func_77946_l());
               heldStack.func_190918_g(1);
               loaded = true;
            }
         } else if (stack.func_77973_b() instanceof ItemCollar) {
            if (tileEntity.getCollar() == null) {
               tileEntity.setCollar(stack.func_77946_l());
               heldStack.func_190918_g(1);
               loaded = true;
            }
         } else if (stack.func_77973_b() instanceof ItemClothes && tileEntity.getClothes() == null) {
            tileEntity.setClothes(stack.func_77946_l());
            heldStack.func_190918_g(1);
            loaded = true;
         }

         if (loaded && loader != null) {
            sendValidMessageToEntity(loader, "Loaded " + stack.func_82833_r());
         }
      }

   }

   public static void setResistance(ItemStack stack, int resistance) {
      if (stack != null && !stack.func_190926_b() && stack.func_77973_b() instanceof IHasResistance) {
         NBTTagCompound nbt = getTagComponent(stack);
         if (nbt != null) {
            int resistanceToSet = resistance >= 0 ? resistance : 0;
            nbt.func_74768_a("resistance", resistanceToSet);
            stack.func_77982_d(nbt);
         }
      }

   }

   public static int getResistance(ItemStack stack, World world) {
      if (stack != null && !stack.func_190926_b() && stack.func_77973_b() instanceof IHasResistance) {
         NBTTagCompound nbt = getTagComponent(stack);
         if (nbt != null && nbt.func_74764_b("resistance")) {
            return nbt.func_74762_e("resistance");
         }

         IHasResistance resistanceItem = (IHasResistance)stack.func_77973_b();
         if (resistanceItem != null) {
            return resistanceItem.getBaseResistance(world);
         }
      }

      return 0;
   }

   public static ItemStack mergeItemsWithresistance(List<ItemStack> stacks, World world, String mergeId) {
      if (mergeId != null && stacks != null && !stacks.isEmpty() && stacks.size() > 1 && world != null) {
         ItemStack maxStack = ItemStack.field_190927_a;
         int maxResistance = 0;
         int newResistance = 0;
         Iterator var6 = stacks.iterator();

         int currentResistance;
         while(var6.hasNext()) {
            ItemStack stack = (ItemStack)var6.next();
            if (stack != null && !stack.func_190926_b() && stack.func_77973_b() instanceof IHasResistance) {
               IHasResistance stackItem = (IHasResistance)stack.func_77973_b();
               if (stackItem != null && stackItem.getResistanceId().equals(mergeId)) {
                  currentResistance = getResistance(stack, world);
                  if (maxStack.func_190926_b() || currentResistance > maxResistance || currentResistance == maxResistance && stack.func_77948_v() && !maxStack.func_77948_v()) {
                     maxStack = stack;
                     maxResistance = currentResistance;
                  }

                  newResistance += currentResistance;
                  continue;
               }

               return ItemStack.field_190927_a;
            }

            return ItemStack.field_190927_a;
         }

         IHasResistance itemResistance = (IHasResistance)maxStack.func_77973_b();
         double mergePercent = (double)itemResistance.getMergePercentRule(world) / 100.0D;
         currentResistance = maxResistance + (int)((double)(newResistance - maxResistance) * mergePercent);
         ItemStack finalStack = maxStack.func_77946_l().func_77979_a(1);
         double maxMerge = (double)UtilsParameters.getResistanceMergeMaxPercent(world);
         if (maxMerge >= 0.0D) {
            double baseResistance = (double)itemResistance.getBaseResistance(world);
            int capResistance = (int)(baseResistance * (maxMerge / 100.0D));
            if (currentResistance > capResistance) {
               currentResistance = capResistance;
            }
         }

         setResistance(finalStack, currentResistance);
         return finalStack;
      } else {
         return ItemStack.field_190927_a;
      }
   }

   public static void playLockSound(EntityPlayer player) {
      playSound(player, KidnapModSoundEvents.COLLAR_KEY_CLOSE, 1.0F);
   }

   public static void playUnlockSound(EntityPlayer player) {
      playSound(player, KidnapModSoundEvents.COLLAR_KEY_OPEN, 1.0F);
   }

   public static void playSound(EntityPlayer player, SoundEvent event, float volume) {
      if (player != null && player.field_70170_p != null && !player.field_70170_p.field_72995_K) {
         player.field_70170_p.func_184133_a((EntityPlayer)null, player.func_180425_c(), event, SoundCategory.AMBIENT, volume, 1.0F);
      }

   }

   public static String getUnlocalizedColorName(Item item, EnumDyeColor mainDye, int meta) {
      String name = item.func_77658_a();
      if (meta > 0 && meta < 16) {
         EnumDyeColor dye = EnumDyeColor.func_176764_b(meta);
         String colorName = dye.func_176762_d();
         if (dye == mainDye) {
            colorName = EnumDyeColor.WHITE.func_176762_d();
         }

         return name + "_" + colorName;
      } else {
         return name;
      }
   }

   public static void sendMessageToOps(World world, String message) {
      if (world != null && !world.field_72995_K) {
         MinecraftServer server = world.func_73046_m();
         if (server != null) {
            PlayerList playersList = server.func_184103_al();
            if (playersList != null) {
               List<EntityPlayerMP> players = playersList.func_181057_v();
               if (players != null) {
                  Iterator var5 = players.iterator();

                  while(var5.hasNext()) {
                     EntityPlayer player = (EntityPlayer)var5.next();
                     if (player != null && isOpe(player)) {
                        player.func_145747_a(new TextComponentString(TextFormatting.YELLOW + message));
                     }
                  }
               }
            }
         }
      }

   }

   public static List<String> getItemNameList(List<? extends Item> list, String arg) {
      List<String> itemsIds = new ArrayList();
      if (arg != null) {
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            Item item = (Item)var3.next();
            String name = item.getRegistryName().toString();
            if (name.contains(arg)) {
               itemsIds.add(name);
            }
         }
      }

      return itemsIds;
   }

   @Nullable
   public static Item getItem(String param, Class<?> cls) {
      if (param != null) {
         Item item = Item.func_111206_d(param);
         if (cls.isInstance(item)) {
            return item;
         }
      }

      return null;
   }

   @Nullable
   public static List<EntityPlayerMP> getWorldPlayers(World world) {
      if (world != null) {
         MinecraftServer server = world.func_73046_m();
         if (server != null) {
            PlayerList list = server.func_184103_al();
            if (list != null) {
               return list.func_181057_v();
            }
         }
      }

      return null;
   }

   public static List<I_Kidnapped> getKidnapableEntitiesAround(World world, BlockPos pos, double distance) {
      List<I_Kidnapped> kidnappeds = new ArrayList();
      if (world != null) {
         List<EntityLivingBase> entitiesList = world.func_72872_a(EntityLivingBase.class, new AxisAlignedBB((double)pos.func_177958_n() - distance, (double)pos.func_177956_o() - distance, (double)pos.func_177952_p() - distance, (double)pos.func_177958_n() + distance, (double)pos.func_177956_o() + distance, (double)pos.func_177952_p() + distance));
         if (entitiesList != null) {
            Iterator var6 = entitiesList.iterator();

            while(true) {
               EntityLivingBase entity;
               do {
                  do {
                     do {
                        if (!var6.hasNext()) {
                           return kidnappeds;
                        }

                        entity = (EntityLivingBase)var6.next();
                     } while(entity == null);
                  } while(entity.field_70170_p == null);
               } while(!(entity instanceof EntityPlayer) && !(entity instanceof I_Kidnapped));

               if (entity.field_70170_p.equals(world)) {
                  I_Kidnapped targetState = null;
                  if (entity instanceof EntityPlayer) {
                     EntityPlayer targetPlayer = (EntityPlayer)entity;
                     targetState = PlayerBindState.getInstance(targetPlayer);
                  } else {
                     targetState = (I_Kidnapped)entity;
                  }

                  kidnappeds.add(targetState);
               }
            }
         }
      }

      return kidnappeds;
   }

   public static Position getEntityPosition(EntityLivingBase entity) {
      if (entity != null) {
         BlockPos posEntity = entity.func_180425_c();
         int dimension = entity.field_71093_bK;
         float yaw = entity.field_70177_z;
         float pitch = entity.field_70125_A;
         Position pos = new Position(dimension, (double)posEntity.func_177958_n(), (double)posEntity.func_177956_o(), (double)posEntity.func_177952_p(), yaw, pitch);
         return pos;
      } else {
         return null;
      }
   }

   public static BlockPos getPositionOfTheNearestBlockAroundEntity(Entity entity, Class<? extends Block> blockType, int radius) {
      BlockPos bestPos = null;
      if (entity != null && entity.field_70170_p != null && blockType != null) {
         BlockPos pos = entity.func_180425_c();
         if (pos != null) {
            for(int i = -radius; i < radius; ++i) {
               for(int j = -radius; j < radius; ++j) {
                  for(int k = -radius; k < radius; ++k) {
                     BlockPos currentPos = pos.func_177982_a(i, j, k);
                     IBlockState block = entity.field_70170_p.func_180495_p(currentPos);
                     if (block != null && block.func_177230_c() != null && blockType.isInstance(block.func_177230_c()) && (bestPos == null || entity.func_174831_c(currentPos) < entity.func_174831_c(bestPos))) {
                        bestPos = currentPos;
                     }
                  }
               }
            }
         }
      }

      return bestPos;
   }

   public static Entity getEntityFromUUID(UUID id) {
      MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
      return server != null ? server.func_175576_a(id) : null;
   }

   public static EntityPlayer getPlayerFromUUID(UUID id) {
      Entity entity = getEntityFromUUID(id);
      if (entity != null && entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         return player;
      } else {
         return null;
      }
   }

   public static Chunk loadChunkFromPosition(Position pos) {
      if (pos == null) {
         return null;
      } else {
         MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
         WorldServer world = mcServer.func_71218_a(pos.getDimension());
         return world != null ? world.func_175726_f(pos.getBlockPos()) : null;
      }
   }

   static {
      biomesForKidnappers = new Biome[]{Biomes.field_76772_c, Biomes.field_76770_e, Biomes.field_76789_p, Biomes.field_76769_d, Biomes.field_76783_v, Biomes.field_76767_f, Biomes.field_76785_t, Biomes.field_76774_n, Biomes.field_76775_o, Biomes.field_150583_P, Biomes.field_150582_Q, Biomes.field_150584_S, Biomes.field_150579_T, Biomes.field_150580_W, Biomes.field_150574_L, Biomes.field_76782_w, Biomes.field_150589_Z, Biomes.field_150585_R, Biomes.field_150588_X, Biomes.field_150587_Y, Biomes.field_76768_g};
   }
}
