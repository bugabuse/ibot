package com.farm.ibot.core;

import com.farm.ibot.api.accessors.defaultinterfaces.*;
import com.farm.ibot.api.accessors.interfaces.*;

public class AccessorInterface {
    public ICamera cameraReflectionInterface = new DefaultCamera();
    public ICanvasAccessor canvasAccessprInterface = new DefaultCanvasAccessor();
    public ICharacter characterReflectionInterface = new DefaultCharacter();
    public IClient clientReflectionInterface = new DefaultClient();
    public ICollisionMap collisionMapReflectionInterface = new DefaultCollisionMap();
    public IComputerInfo computerInfoInterface = new DefaultComputerInfo();
    public IConfig configInterface = new DefaultConfig();
    public IEncryptedStream encryptedStreamInterface = new DefaultEncryptedStream();
    public IGameConfig gameConfigInterface = new DefaultGameConfig();
    public IGameShell gameShellInterface = new DefaultGameShell();
    public IGrandExchangeItem grandExchangeItemInterface = new DefaultGrandExchangeItem();
    public IGroundItem groundItemInterface = new DefaultGroundItem();
    public IGroundItems groundItemsInterface = new DefaultGroundItems();
    public IHashTable hashTableInterface = new DefaultHashTable();
    public IIsaacWrapper isaacWrapperInterface = new DefaultIsaacWrapper();
    public IItemLayer itemLayerInterface = new DefaultItemLayer();
    public ILinkedList linkedListInterface = new DefaultLinkedList();
    public IMenu menuInterface = new DefaultMenu();
    public IModel modelInterface = new DefaultModel();
    public INode nodeInterface = new DefaultNode();
    public INpc npcInterface = new DefaultNpc();
    public INpcComposite npcCompositeInterface = new DefaultNpcComposite();
    public IPlayer playerInterface = new DefaultPlayer();
    public IPlayerComposite playerCompositeInterface = new DefaultPlayerComposite();
    public IProjectile projectileInterface = new DefaultProjectile();
    public IRegion regionInterface = new DefaultRegion();
    public IRegionTile regionTileInterface = new DefaultRegionTile();
    public IRenderable renderableInterface = new DefaultRenderable();
    public IStream streamInterface = new DefaultStream();
    public IWidget widgetInterface = new DefaultWidget();
    public IWidgetNode widgetNodeInterface = new DefaultWidgetNode();
    public IWorld worldInterface = new DefaultWorld();
    public IBoundaryObject boundaryObjectReflectionInterface = new DefaultBoundaryObject();
    public IFloorObject floorObjectReflectionInterface = new DefaultFloorObject();
    public IGameObject gameObjectReflectionInterface = new DefaultGameObject();
    public IWallObject wallObjectReflectionInterface = new DefaultWallObject();
    public IMouseTracker mouseTrackerReflectionInterface = new DefaultMouseTracker();
    public IHealthBar healthBar = new DefaultHealthBar();
    public IHealthBarData healthBarData = new DefaultHealthBarData();
    public IIterable iterableInterface = new DefaultIterable();
}
