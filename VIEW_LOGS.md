# Как посмотреть логи Android приложения

## Способ 1: Android Studio Logcat (Рекомендуется)

1. Откройте Android Studio
2. Внизу экрана найдите вкладку **"Logcat"**
3. Выберите ваше устройство/эмулятор в выпадающем списке
4. Используйте фильтры:
   - По тегу: `MapKit`, `YandexMap`, `BarsMapFragment`
   - По уровню: `Error`, `Warning`
   - По пакету: `com.example.zero_degree`

## Способ 2: Через терминал (adb logcat)

### Найти путь к adb:
```bash
# macOS
~/Library/Android/sdk/platform-tools/adb

# Linux
$ANDROID_HOME/platform-tools/adb

# Или если ANDROID_HOME не установлен
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

### Полезные команды:

**Все логи:**
```bash
adb logcat
```

**Только ошибки:**
```bash
adb logcat *:E
```

**Логи вашего приложения:**
```bash
adb logcat | grep "zero_degree"
```

**Логи MapKit:**
```bash
adb logcat | grep -i "mapkit\|yandex"
```

**Очистить логи и начать заново:**
```bash
adb logcat -c && adb logcat
```

**Сохранить логи в файл:**
```bash
adb logcat > logs.txt
```

**Фильтр по тегам:**
```bash
adb logcat MapKit:D BarsMapFragment:D *:S
```

## Способ 3: Через Android Studio Device Monitor

1. Tools → Device Manager
2. Выберите устройство
3. View → Tool Windows → Logcat

## Полезные теги для отладки карты:

- `MapKit` - логи Яндекс MapKit
- `BarsMapFragment` - логи фрагмента карты
- `YandexMap` - общие логи карты
- `AndroidRuntime` - краши приложения

## Фильтры для поиска проблем с картой:

```bash
# Все ошибки
adb logcat *:E

# Логи MapKit и вашего приложения
adb logcat MapKit:D com.example.zero_degree:D *:S

# Только ошибки и предупреждения
adb logcat *:W
```

