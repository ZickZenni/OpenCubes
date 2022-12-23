[<img src="src/main/resources/assets/textures/gui/logo.png" width="517"/>]()

# OpenCubes

A voxel game, that tries to bring back the old Minecraft Alpha days and make it better

## Versions

I am currently working only on one version: Java

Sometime later i will bring a better version using C++

## Java
I currently use **Java 17**, a more recent version than what Minecraft Alpha uses.

It's currently **work in progress**, so there will be many bugs and probably lags. Reporting them will be really helpful

### Todo

#### Renderer

- [x] Rect Rendering
- [x] Font Rendering
- [x] Image Rendering
- [ ] 3d Rendering on UI

#### Blocks

- [ ] Load Block Models from resources

#### UI

- [x] Debug Menu (F3)
- [ ] Main Menu (Partially done)
- [ ] World Menu

#### World

- [ ] World creation
- [ ] Better world generation
- [ ] Biomes
- [ ] Lighting Engine

#### Entities

- [ ] Player Controller

#### Player

- [ ] Creative
- [ ] Hotbar
- [ ] Player Inventory
- [x] Block Placing/Breaking

#### Util

- [x] Raycasting

#### Other

- [x] Sound System
- [ ] Game Settings (Currently only static)
- [x] Log System
- [ ] Saving Logs to Disk
- [x] Locked Mouse
- [x] Crosshair

## Developement

### IntelliJ

Open build.gradle as a project and you can start working instantly.

### Building and Running

Currently there is no buttons for the main menu, so if you want to play in a world press F.


If you want to run the client:

```
gradlew runClient
```

And build the game:

```
gradlew build
```


Starting the game requires a username argument, something like this:

```
java -jar (jarLocation) --username Dev
```