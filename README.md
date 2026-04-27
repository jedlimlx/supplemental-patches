# Supplemental Patches

<p>
<a class="no-svg" href="https://modrinth.com/mod/supplemental-patches" target="_blank" rel="noopener noreferrer"><img class="not-clickable" src="https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/available/modrinth_vector.svg" alt="Modrinth" style="padding-right:5px; padding-left:5px; margin: 0 0;"></a>
<a class="no-svg" href="https://www.curseforge.com/minecraft/mc-mods/supplemental-patches" target="_blank" rel="noopener noreferrer"><img class="not-clickable"  src="https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/available/curseforge_vector.svg" alt="CurseForge" style="padding-right:5px ;padding-left:5px; margin: 0 0;"></a>
<a class="no-svg" href="https://github.com/jedlimlx/supplemental-patches/issues" target="_blank" rel="noopener noreferrer"><img class="not-clickable"  src="https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/documentation/issues_vector.svg" alt="Issue Tracker" style="padding-right:5px; padding-left:5px; margin: 0 0;"></a>
</p>

<img src="https://supplemental-patches.pages.dev/assets/banner.png">

Supplemental Patches enables users to create custom shaders for modded blocks, entities, items, particles, etc,
through a resource-pack. The mod by default, comes with a built-in resource pack with supports many popular mods (see [gallery](https://supplemental-patches.pages.dev/gallery)).

These additional shaders are patched into [Euphoria Patches](https://www.euphoriapatches.com/), if it is installed.

## Features

Some of the most _exciting_ features of the mod include (for players):

- Over 500 high-quality shaders for custom materials from a variety of popular mods (e.g. [Supplementaries](https://modrinth.com/mod/supplementaries), [Quark](https://modrinth.com/mod/quark), [Jaden's Nether Expansion](https://modrinth.com/mod/jadens-nether-expansion)). See full list [here](https://supplemental-patches.pages.dev/mod_list).
- A custom End nebula skybox based on [Enderscape's](https://modrinth.com/mod/enderscape) End atmosphere
- A cool fog effect based on the effect from [Doom and Gloom](https://modrinth.com/mod/doom-gloom)
- Fixes to shader issues for many mods (e.g. [Endergetic Expansion](https://modrinth.com/mod/endergetic), [Upgrade Aquatic](https://modrinth.com/mod/upgrade-aquatic), [Doom and Gloom](https://modrinth.com/mod/doom-gloom))
- Fixed rendering of item shaders when displayed on block entities (e.g. [Supplementaries](https://modrinth.com/mod/supplementaries) Pedestals, Item Shelves)

For details on the supported areas that can be patched onto Euphoria Patches, check out this [tutorial](https://supplemental-patches.pages.dev/tutorials/getting_started).

## Building the Mod

This projects uses the [Stonecutter](https://stonecutter.kikugie.dev/) Gradle Plugin, building off [Rotgruengelb's template](https://github.com/rotgruengelb/stonecutter-mod-template) 
to handle multiloader. To run the mod on the client, use the command (modify it to the relevant minecraft version)

```
./gradlew 1.21.1-neoForge:runClient
```

To build the mod, run (the minecraft version can be removed to build the mod for all targets)
```
./gradlew 1.21.1-neoForge:build
```

If you have access to a development version of Euphoria Patches, it can be loaded by creating a secrets.properties and placing the 
link to the shaderpack as such
```properties
deps.euphoria-dev=[LINK HERE]
```

The mod will automatically patch onto the development version.

## FAQ

**Q:** Will there be ports to loaders other than (Neo)Forge and Fabric? <br>
**A:** Probably not.

**Q:** Will there be backports? <br>
**A:** Probably not.

**Q:** Can you support XXX mod? <br>
**A:** All in due time. Alternatively, you could do it yourself. Have a look at this [tutorial](https://supplemental-patches.pages.dev/tutorials/getting_started) for more details.

**Q:** I found a bug. <br>
**A:** Please report it to me first, do not bother the developers of Complementary Shaders / Euphoria Patches.

**Q:** The shaders I added aren't showing. <br>
**A:** Try reloading the shaderpack, after ensuring that the resource-pack is loaded. Alternatively, check which layer the block is being rendered in using the [Euphoria Companion](https://modrinth.com/mod/euphoria-companion) mod. Certain blocks (e.g. certain glass blocks) may be rendered as cutouts rather than translucent blocks. You will need to change the layer they are rendered under using [block.properties.json](documentation/properties.json).

**Q:** The built-in shaders aren't working. <br>
**A:** Similarly, try reloading the shaderpack and ensuring that the built-in resource-pack is loaded.

**Q:** Does this work with Optifine? <br>
**A:** No. This only works with Oculus / Iris.

**Q:** Can I include this my modpack? <br>
**A:** Yes, go ahead.
