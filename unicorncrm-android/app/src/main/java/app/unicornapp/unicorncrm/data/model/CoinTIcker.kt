package app.unicornapp.unicorncrm.data.model

data class CoinTicker(
    val id: String,
    val name: String,
    val symbol: String,
    val rank: Int,
    val total_supply: Long?,
    val max_supply: Long?,
    val beta_value: Double?,
    val first_data_at: String?,
    val last_updated: String?,
    val quotes: Map<String, Quote>
)

data class Quote(
    val price: Double,
    val volume_24h: Double,
    val volume_24h_change_24h: Double,
    val market_cap: Double,
    val market_cap_change_24h: Double,
    val percent_change_15m: Double,
    val percent_change_30m: Double,
    val percent_change_1h: Double,
    val percent_change_6h: Double,
    val percent_change_12h: Double,
    val percent_change_24h: Double,
    val percent_change_7d: Double,
    val percent_change_30d: Double,
    val percent_change_1y: Double,
    val ath_price: Double,
    val ath_date: String,
    val percent_from_price_ath: Double
)