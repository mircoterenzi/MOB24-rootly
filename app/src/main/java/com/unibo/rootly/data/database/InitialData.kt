package com.unibo.rootly.data.database

object InitialData {
    fun getInitialSpecies(): List<Species> {
        return listOf(
            Species("Spider Plant", 2, 2, 2, 25.0f, 10.0f),
            Species("Snake Plant", 3, 1, 1, 30.0f, 15.0f),
            Species("Pothos", 2, 1, 3, 25.0f, 15.0f),
            Species("Peace Lily", 2, 1, 3, 25.0f, 15.0f),
            Species("ZZ Plant", 4, 1, 1, 30.0f, 15.0f),
            Species("Monstera", 2, 2, 2, 30.0f, 15.0f),
            Species("Philodendron", 2, 2, 3, 25.0f, 15.0f),
            Species("Rubber Plant", 2, 2, 2, 25.0f, 10.0f),
            Species("Fiddle Leaf Fig", 2, 3, 3, 25.0f, 15.0f),
            Species("Aloe Vera", 4, 2, 4, 35.0f, 10.0f),
            Species("Snake Plant", 4, 2, 2, 30.0f, 15.0f),
            Species("English Ivy", 3, 1, 3, 25.0f, 15.0f),
            Species("Chinese Evergreen", 2, 1, 3, 25.0f, 15.0f),
            Species("Parlor Palm", 2, 1, 3, 25.0f, 15.0f),
            Species("Fern", 2, 2, 3, 25.0f, 15.0f),
            Species("Jade Plant", 4, 2, 4, 35.0f, 10.0f),
            Species("Succulent", 4, 2, 4, 35.0f, 10.0f),
            Species("Calathea", 2, 2, 2, 25.0f, 15.0f),
            Species("Bird of Paradise", 2, 3, 3, 30.0f, 15.0f),
            Species("Money Tree", 3, 2, 4, 30.0f, 15.0f),
            Species("Dracaena", 3, 2, 3, 30.0f, 15.0f),
            Species("Pilea", 4, 1, 3, 25.0f, 15.0f),
            Species("Spider Plant", 2, 2, 2, 25.0f, 10.0f),
            Species("Christmas Cactus", 2, 1, 2, 25.0f, 10.0f),
            Species("Hoya", 3, 1, 3, 25.0f, 15.0f),
            Species("Dieffenbachia", 2, 1, 3, 25.0f, 15.0f),
            Species("Oxalis", 2, 1, 3, 25.0f, 15.0f),
            Species("Bromeliad", 2, 2, 2, 25.0f, 15.0f),
            Species("Schefflera", 2, 1, 3, 25.0f, 15.0f),
            Species("String of Pearls", 4, 1, 4, 35.0f, 10.0f)
        )
    }

    fun getInitialBadgeTypes(): List<BadgeType> {
        //todo real badges
        return listOf(
            BadgeType("badge1", "badge1_image", "Badge 1 description"),
            BadgeType("badge2", "badge2_image", "Badge 2 description")
        )
    }

    fun getInitialUser(): User {
        return User(userId = 1, username = "user1", location = "Location 1", profileImg = "path_to_profile_image", numberOfPlants = 10)
    }

    fun getInitialPlants(): List<Plant> {
        val plants = mutableListOf<Plant>()
        for (i in 1..10) {
            val species = if (i % 2 == 0) {
                getInitialSpecies()[0] // Spider Plant
            } else {
                getInitialSpecies()[1] // Snake Plant
            }
            plants.add(
                Plant(
                    userId = 1,
                    plantId = i,
                    plantName = "Plant $i",
                    isDead = 'N',
                    birthday = "2022-01-01", // Adjust the birthday according to your needs
                    scientificName = species.scientificName
                )
            )
        }
        return plants
    }

    //example to test the app

    fun getInitialWaters(): List<Water> {
        val waters = mutableListOf<Water>()
        for (i in 1..10) {
            val waterDate = "2022-01-${i + 5}"
            waters.add(Water(userId = 1, plantId = i, date = waterDate))
        }
        return waters
    }

    fun getInitialFertilizerss(): List<Fertilizer> {
        val fertilizers = mutableListOf<Fertilizer>()
        for (i in 1..10) {
            val fertilizerDate = "2022-01-${i + 15}"
            fertilizers.add(Fertilizer(userId = 1, plantId = i, date = fertilizerDate))
        }
        return fertilizers
    }

    fun getInitialPlantLogs(): List<PlantLog> {
        val plantLogs = mutableListOf<PlantLog>()
        for (i in 1..10) {
            val logDate = "2022-01-${i + 10}"
            plantLogs.add(
                PlantLog(
                    userId = 1,
                    plantId = i,
                    date = logDate,
                    description = "Log for Plant $i",
                    picture = "path_to_picture_$i",
                    height = 15.0f // Adjust the height according to your needs
                )
            )
        }
        return plantLogs
    }

}