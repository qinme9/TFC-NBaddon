# TFC:NBAddon

> TerraFirmaCraft 附属模组 —— 为你的群峦世界添加冰箱与超级营养食物。

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-green)](https://www.minecraft.net/)
[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.234-orange)](https://neoforged.net/)
[![License](https://img.shields.io/badge/License-All%20Rights%20Reserved-red)](LICENSE)

---

## 内容

### 冰箱 (Fridge)

一个 54 格的大容量储存方块，具备 **TFC 保鲜功能** —— 放入其中的食物腐坏速度大幅减缓。

| 配方 | 说明 |
|------|------|
| 8 锻铁锭 + 1 盐 | 3x3 合成，盐放中间 |
| 石镐及以上可挖掘 | 硬度 1.0，掉落自身 |

**保鲜机制：** 每秒为容器内所有带有 TFC `food` 组件的物品延长 `creationDate`，等效于减缓腐烂。（效果取决于 TFC 的 `FoodCapability` 系统）

---

### 超级营养食物 (Super Nutrition Food)

一种可配置营养数值的万能食物。

| 配方 | 说明 |
|------|------|
| 9 个粪石 (Guano) | 无序合成 |

**可配置项：** 通过模组配置文件可调节谷物/水果/蔬菜/蛋白质/乳制品营养值、口渴值、饥饿值、饱和度以及腐烂速度。

---

## 配置文件

配置文件位于 `config/tfcnbaddon-common.toml`：

```toml
[superFood]
grain = 0.8       # 谷物营养
fruit = 0.8       # 水果营养
vegetables = 0.8  # 蔬菜营养
protein = 0.8     # 蛋白质营养
dairy = 0.8       # 乳制品营养
water = 10        # 口渴值
hunger = 8        # 饥饿值
saturation = 0.6  # 饱和度
decay = 0.25      # 腐烂速度 (0 = 不腐烂)
```

---

## 依赖

- **TerraFirmaCraft** (TFC) for 1.21.1
- **NeoForge** 21.1.234+

---

## 构建

```bash
./gradlew build
```

构建产物位于 `build/libs/`。

---

## 许可

All Rights Reserved
