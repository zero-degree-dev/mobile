# Архитектура приложения ZERO°

## Структура проекта

Приложение построено на архитектуре **MVVM** (Model-View-ViewModel) с использованием **Jetpack Compose**.

### Основные компоненты:

1. **Data Layer (Слой данных)**
   - `data/model/` - модели данных (Drink, Bar, Event, Booking, User, Review)
   - `data/api/` - API сервисы (Retrofit)
   - `data/repository/` - репозитории для работы с данными

2. **UI Layer (Слой интерфейса)**
   - `ui/screens/` - Compose экраны
   - `ui/viewmodel/` - ViewModel классы
   - `ui/theme/` - тема приложения (цвета, типографика)

3. **Navigation (Навигация)**
   - `navigation/NavGraph.kt` - навигационный граф

## Основные экраны:

1. **HomeScreen** - главный экран с приветствием, быстрыми действиями, последними барами и событиями
2. **DrinkCatalogScreen** - каталог напитков с фильтрацией
3. **DrinkDetailScreen** - детальная страница напитка с отзывами
4. **BarsMapScreen** - карта/список баров
5. **BookingScreen** - экран бронирования столика

## Используемые технологии:

- **Jetpack Compose** - для UI
- **Retrofit** - для сетевых запросов
- **Coroutines & Flow** - для асинхронных операций и реактивного обновления UI
- **Navigation Component** - для навигации между экранами
- **Coil** - для загрузки изображений
- **Material 3** - для компонентов UI

## Настройка API:

В файле `data/api/RetrofitClient.kt` нужно указать правильный базовый URL API:

```kotlin
private const val BASE_URL = "https://api.zero-degree.com/api/"
```

## Цветовая схема:

- **Amber** (#FFB300) - янтарный (основной цвет)
- **Ivory** (#FFF8E1) - цвет слоновой кости
- **Graphite** (#424242) - графитовый
- **Foam** (#F5F5F5) - оттенок пены

## Следующие шаги:

1. Настроить реальный API endpoint
2. Добавить авторизацию (JWT токены)
3. Реализовать экраны событий и профиля
4. Добавить Room для локального кэширования
5. Добавить Google Maps для карты баров
6. Реализовать админку



