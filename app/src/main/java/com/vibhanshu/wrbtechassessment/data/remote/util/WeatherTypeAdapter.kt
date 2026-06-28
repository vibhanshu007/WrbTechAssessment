package com.vibhanshu.wrbtechassessment.data.remote.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.vibhanshu.wrbtechassessment.domain.model.WeatherType
import java.lang.reflect.Type

class WeatherTypeAdapter : JsonSerializer<WeatherType>, JsonDeserializer<WeatherType> {
    override fun serialize(src: WeatherType, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.description)
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): WeatherType {
        val description = json.asString
        return when (description) {
            WeatherType.ClearSky.description -> WeatherType.ClearSky
            WeatherType.MainlyClear.description -> WeatherType.MainlyClear
            WeatherType.PartlyCloudy.description -> WeatherType.PartlyCloudy
            WeatherType.Overcast.description -> WeatherType.Overcast
            WeatherType.Foggy.description -> WeatherType.Foggy
            WeatherType.DepositingRimeFog.description -> WeatherType.DepositingRimeFog
            WeatherType.LightDrizzle.description -> WeatherType.LightDrizzle
            WeatherType.ModerateDrizzle.description -> WeatherType.ModerateDrizzle
            WeatherType.DenseDrizzle.description -> WeatherType.DenseDrizzle
            WeatherType.LightFreezingDrizzle.description -> WeatherType.LightFreezingDrizzle
            WeatherType.DenseFreezingDrizzle.description -> WeatherType.DenseFreezingDrizzle
            WeatherType.SlightRain.description -> WeatherType.SlightRain
            WeatherType.ModerateRain.description -> WeatherType.ModerateRain
            WeatherType.HeavyRain.description -> WeatherType.HeavyRain
            WeatherType.HeavyFreezingRain.description -> WeatherType.HeavyFreezingRain
            WeatherType.LightFreezingRain.description -> WeatherType.LightFreezingRain
            WeatherType.SlightSnowFall.description -> WeatherType.SlightSnowFall
            WeatherType.ModerateSnowFall.description -> WeatherType.ModerateSnowFall
            WeatherType.HeavySnowFall.description -> WeatherType.HeavySnowFall
            WeatherType.SnowGrains.description -> WeatherType.SnowGrains
            WeatherType.SlightRainShowers.description -> WeatherType.SlightRainShowers
            WeatherType.ModerateRainShowers.description -> WeatherType.ModerateRainShowers
            WeatherType.ViolentRainShowers.description -> WeatherType.ViolentRainShowers
            WeatherType.SlightSnowShowers.description -> WeatherType.SlightSnowShowers
            WeatherType.HeavySnowShowers.description -> WeatherType.HeavySnowShowers
            WeatherType.ModerateThunderstorm.description -> WeatherType.ModerateThunderstorm
            WeatherType.SlightThunderstormWithHail.description -> WeatherType.SlightThunderstormWithHail
            WeatherType.HeavyThunderstormWithHail.description -> WeatherType.HeavyThunderstormWithHail
            else -> WeatherType.ClearSky
        }
    }
}
