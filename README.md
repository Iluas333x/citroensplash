# CitroenSplash

A lightweight Android app for car head units: shows a full-screen welcome
screen for a few seconds right after boot, then hands off to the unit's
normal launcher automatically. Includes a settings screen to enable/disable
it and adjust the duration.

## ⚠️ About the logo

This project ships with a **plain placeholder graphic**, not an actual
Citroën logo — that artwork is a registered trademark, and I can't generate
or embed it for you. The placeholder sits in exactly the spot, size, and
file format your real logo needs, so swapping it in is a one-step change.

To use your own artwork:

1. Get a high-resolution image file you have the rights to use (ideally a
   PNG with a transparent background, **1024×1024px or larger**).
2. Delete `app/src/main/res/drawable/splash_logo.xml`.
3. Add your file at `app/src/main/res/drawable-nodpi/splash_logo.png`
   (the `-nodpi` folder stops Android from downscaling it based on screen
   density, preserving full quality).
4. Rebuild — no code changes needed, `activity_splash.xml` already points
   at `@drawable/splash_logo`.

## What's in the project

| File | Purpose |
|---|---|
| `BootReceiver.kt` | Listens for `BOOT_COMPLETED` (and vendor "quick boot" equivalents), launches the splash if enabled. |
| `SplashActivity.kt` | Full-screen immersive welcome screen; auto-dismisses to the launcher after the configured duration. |
| `SettingsActivity.kt` | The only launchable screen — toggle on/off, adjust duration (2–10s, default 4s). |
| `Prefs.kt` | Tiny SharedPreferences wrapper holding the two settings. |
| `res/drawable/splash_logo.xml` | Placeholder logo — replace per above. |

## Building it

1. Open the `CitroenSplash` folder in Android Studio ("Open an existing
   project"). Android Studio will detect the missing Gradle wrapper jar and
   offer to generate it automatically — accept that prompt (or run
   `gradle wrapper` yourself first if you have Gradle installed).
2. Let it sync, then **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
3. Before shipping, change `applicationId` in `app/build.gradle` from the
   placeholder `com.example.citroensplash` to your own package name.

## Installing on the head unit

Most aftermarket Android head units accept a normal APK install via USB/ADB
or a file manager + "install unknown apps":

```
adb connect <head-unit-ip-or-just-plug-in-usb>
adb install -r app-release.apk
```

After installing, **open the app once manually** (tap its icon — it goes to
the Settings screen) before rebooting. On stock Android, a freshly installed
app is in a "stopped" state and won't receive `BOOT_COMPLETED` until it's
been launched at least once. This is a one-time step per install, not per
boot.

## Head-unit-specific notes

- **Quick-boot ROMs**: many head unit vendors send
  `android.intent.action.QUICKBOOT_POWERON` instead of (or alongside)
  `BOOT_COMPLETED` on fast-boot systems. Both are handled already.
- **Aggressive autostart restrictions**: some heavily customized vendor
  ROMs (common on Chinese aftermarket units) ship a MIUI-style "autostart"
  permission toggle, separate from normal Android permissions. If the
  splash doesn't appear after reboot, check the system settings app for an
  autostart/self-start permission list and enable it there for this app.
- **Testing without a full reboot**: you can preview the splash directly:
  ```
  adb shell am start -n com.example.citroensplash/.SplashActivity
  ```
  (substitute your real `applicationId` if you changed it).
- **Resource usage**: there's no service and no background process — the
  receiver does one SharedPreferences read and starts one Activity, which
  then runs a single delayed callback before closing itself. This adds no
  measurable overhead to boot time.

## Settings

Tap the app icon (labeled "Splash Settings") any time to open the settings
screen: a switch to enable/disable the welcome screen, and a slider for
duration in seconds (2–10, default 4).
