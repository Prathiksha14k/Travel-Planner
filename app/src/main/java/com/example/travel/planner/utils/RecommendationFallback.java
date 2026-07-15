package com.example.travel.planner.utils;

import com.example.travel.planner.models.Recommendation;
import java.util.ArrayList;
import java.util.List;

public class RecommendationFallback {

    // Returns hardcoded recommendations for known cities
    public static List<Recommendation> getFor(String cityName) {
        if (cityName == null) return getDefault();
        String lower = cityName.toLowerCase();

        if (lower.contains("paris") || lower.contains("france"))
            return getParis();
        if (lower.contains("london") || lower.contains("england"))
            return getLondon();
        if (lower.contains("tokyo") || lower.contains("japan"))
            return getTokyo();
        if (lower.contains("new york") || lower.contains("manhattan"))
            return getNewYork();
        if (lower.contains("dubai") || lower.contains("uae"))
            return getDubai();
        if (lower.contains("rome") || lower.contains("italy"))
            return getRome();
        if (lower.contains("bali") || lower.contains("indonesia"))
            return getBali();
        if (lower.contains("singapore"))
            return getSingapore();
        if (lower.contains("bangkok") || lower.contains("thailand"))
            return getBangkok();
        if (lower.contains("sydney") || lower.contains("australia"))
            return getSydney();
        if (lower.contains("delhi") || lower.contains("india"))
            return getDelhi();
        if (lower.contains("mumbai"))
            return getMumbai();
        if (lower.contains("barcelona") || lower.contains("spain"))
            return getBarcelona();
        if (lower.contains("amsterdam") || lower.contains("netherlands"))
            return getAmsterdam();
        if (lower.contains("istanbul") || lower.contains("turkey"))
            return getIstanbul();
        if (lower.contains("cairo") || lower.contains("egypt"))
            return getCairo();
        if (lower.contains("prague") || lower.contains("czech"))
            return getPrague();
        if (lower.contains("san francisco") || lower.contains("california"))
            return getSanFrancisco();
        if (lower.contains("new zealand") || lower.contains("auckland"))
            return getNewZealand();
        if (lower.contains("maldives"))
            return getMaldives();

        return getDefault();
    }

    // ─── PARIS ───────────────────────────────────────────
    private static List<Recommendation> getParis() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Eiffel Tower",
                "🏛️ Tourist Spot", "tourism",
                48.8584, 2.2945, "Champ de Mars"));
        list.add(make("Louvre Museum",
                "🏛️ Tourist Spot", "tourism",
                48.8606, 2.3376, "Rue de Rivoli"));
        list.add(make("Notre-Dame Cathedral",
                "🏛️ Tourist Spot", "tourism",
                48.8530, 2.3499, "Île de la Cité"));
        list.add(make("Arc de Triomphe",
                "🏛️ Tourist Spot", "tourism",
                48.8738, 2.2950, "Place Charles de Gaulle"));
        list.add(make("Sacré-Cœur Basilica",
                "🏛️ Tourist Spot", "tourism",
                48.8867, 2.3431, "Montmartre"));
        list.add(make("Café de Flore",
                "🍽️ Restaurant", "amenity",
                48.8540, 2.3330, "Saint-Germain-des-Prés"));
        list.add(make("Le Jules Verne",
                "🍽️ Restaurant", "amenity",
                48.8583, 2.2944, "Eiffel Tower, 2nd floor"));
        list.add(make("Ladurée",
                "🍽️ Restaurant", "amenity",
                48.8726, 2.3061, "Champs-Élysées"));
        list.add(make("Hôtel Le Bristol",
                "🏨 Hotel", "tourism",
                48.8737, 2.3133, "Rue du Faubourg"));
        list.add(make("Le Marais Hotel",
                "🏨 Hotel", "tourism",
                48.8570, 2.3556, "Le Marais District"));
        return setDistances(list, 48.8566, 2.3522);
    }

    // ─── LONDON ──────────────────────────────────────────
    private static List<Recommendation> getLondon() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Big Ben",
                "🏛️ Tourist Spot", "tourism",
                51.5007, -0.1246, "Westminster"));
        list.add(make("Tower Bridge",
                "🏛️ Tourist Spot", "tourism",
                51.5055, -0.0754, "Tower Hill"));
        list.add(make("Buckingham Palace",
                "🏛️ Tourist Spot", "tourism",
                51.5014, -0.1419, "Westminster"));
        list.add(make("British Museum",
                "🏛️ Tourist Spot", "tourism",
                51.5194, -0.1270, "Bloomsbury"));
        list.add(make("Hyde Park",
                "🏛️ Tourist Spot", "tourism",
                51.5073, -0.1657, "Kensington"));
        list.add(make("The Ritz Restaurant",
                "🍽️ Restaurant", "amenity",
                51.5072, -0.1425, "Piccadilly"));
        list.add(make("Sketch London",
                "🍽️ Restaurant", "amenity",
                51.5120, -0.1437, "Mayfair"));
        list.add(make("Gordon Ramsay Restaurant",
                "🍽️ Restaurant", "amenity",
                51.4855, -0.1667, "Chelsea"));
        list.add(make("The Savoy Hotel",
                "🏨 Hotel", "tourism",
                51.5099, -0.1204, "Strand"));
        list.add(make("Claridge's Hotel",
                "🏨 Hotel", "tourism",
                51.5125, -0.1486, "Mayfair"));
        return setDistances(list, 51.5074, -0.1278);
    }

    // ─── TOKYO ───────────────────────────────────────────
    private static List<Recommendation> getTokyo() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Senso-ji Temple",
                "🏛️ Tourist Spot", "tourism",
                35.7148, 139.7967, "Asakusa"));
        list.add(make("Tokyo Tower",
                "🏛️ Tourist Spot", "tourism",
                35.6586, 139.7454, "Minato"));
        list.add(make("Shibuya Crossing",
                "🏛️ Tourist Spot", "tourism",
                35.6595, 139.7004, "Shibuya"));
        list.add(make("Meiji Shrine",
                "🏛️ Tourist Spot", "tourism",
                35.6764, 139.6993, "Harajuku"));
        list.add(make("Tokyo Disneyland",
                "🏛️ Tourist Spot", "tourism",
                35.6329, 139.8804, "Urayasu"));
        list.add(make("Sukiyabashi Jiro",
                "🍽️ Restaurant", "amenity",
                35.6717, 139.7640, "Ginza"));
        list.add(make("Ichiran Ramen Shibuya",
                "🍽️ Restaurant", "amenity",
                35.6596, 139.6984, "Shibuya"));
        list.add(make("Tsukiji Outer Market",
                "🍽️ Restaurant", "amenity",
                35.6654, 139.7707, "Tsukiji"));
        list.add(make("Park Hyatt Tokyo",
                "🏨 Hotel", "tourism",
                35.6861, 139.6922, "Shinjuku"));
        list.add(make("The Peninsula Tokyo",
                "🏨 Hotel", "tourism",
                35.6717, 139.7584, "Marunouchi"));
        return setDistances(list, 35.6762, 139.6503);
    }

    // ─── NEW YORK ─────────────────────────────────────────
    private static List<Recommendation> getNewYork() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Statue of Liberty",
                "🏛️ Tourist Spot", "tourism",
                40.6892, -74.0445, "Liberty Island"));
        list.add(make("Central Park",
                "🏛️ Tourist Spot", "tourism",
                40.7851, -73.9683, "Manhattan"));
        list.add(make("Times Square",
                "🏛️ Tourist Spot", "tourism",
                40.7580, -73.9855, "Midtown Manhattan"));
        list.add(make("Empire State Building",
                "🏛️ Tourist Spot", "tourism",
                40.7484, -73.9967, "Midtown"));
        list.add(make("Brooklyn Bridge",
                "🏛️ Tourist Spot", "tourism",
                40.7061, -73.9969, "Brooklyn"));
        list.add(make("Eleven Madison Park",
                "🍽️ Restaurant", "amenity",
                40.7416, -73.9872, "Flatiron"));
        list.add(make("Katz's Delicatessen",
                "🍽️ Restaurant", "amenity",
                40.7223, -73.9873, "Lower East Side"));
        list.add(make("The Plaza Hotel",
                "🏨 Hotel", "tourism",
                40.7645, -73.9744, "Central Park South"));
        list.add(make("The Standard High Line",
                "🏨 Hotel", "tourism",
                40.7404, -74.0087, "Meatpacking District"));
        return setDistances(list, 40.7128, -74.0060);
    }

    // ─── DUBAI ───────────────────────────────────────────
    private static List<Recommendation> getDubai() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Burj Khalifa",
                "🏛️ Tourist Spot", "tourism",
                25.1972, 55.2744, "Downtown Dubai"));
        list.add(make("Palm Jumeirah",
                "🏛️ Tourist Spot", "tourism",
                25.1124, 55.1390, "Palm Jumeirah"));
        list.add(make("Dubai Mall",
                "🏛️ Tourist Spot", "tourism",
                25.1980, 55.2796, "Downtown Dubai"));
        list.add(make("Dubai Desert Safari",
                "🏛️ Tourist Spot", "tourism",
                24.9857, 55.3093, "Dubai Desert"));
        list.add(make("Gold Souk",
                "🏛️ Tourist Spot", "tourism",
                25.2854, 55.3047, "Deira"));
        list.add(make("Nobu Dubai",
                "🍽️ Restaurant", "amenity",
                25.1922, 55.2668, "Atlantis The Palm"));
        list.add(make("At.mosphere Restaurant",
                "🍽️ Restaurant", "amenity",
                25.1972, 55.2744, "Burj Khalifa 122nd floor"));
        list.add(make("Burj Al Arab",
                "🏨 Hotel", "tourism",
                25.1412, 55.1853, "Jumeirah Beach"));
        list.add(make("Atlantis The Palm",
                "🏨 Hotel", "tourism",
                25.1302, 55.1174, "Palm Jumeirah"));
        return setDistances(list, 25.2048, 55.2708);
    }

    // ─── ROME ────────────────────────────────────────────
    private static List<Recommendation> getRome() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Colosseum",
                "🏛️ Tourist Spot", "tourism",
                41.8902, 12.4922, "Piazza del Colosseo"));
        list.add(make("Trevi Fountain",
                "🏛️ Tourist Spot", "tourism",
                41.9009, 12.4833, "Trevi"));
        list.add(make("Vatican Museums",
                "🏛️ Tourist Spot", "tourism",
                41.9065, 12.4536, "Vatican City"));
        list.add(make("Pantheon",
                "🏛️ Tourist Spot", "tourism",
                41.8986, 12.4769, "Piazza della Rotonda"));
        list.add(make("Roman Forum",
                "🏛️ Tourist Spot", "tourism",
                41.8925, 12.4853, "Via Sacra"));
        list.add(make("La Pergola",
                "🍽️ Restaurant", "amenity",
                41.9074, 12.4581, "Rome Cavalieri Hotel"));
        list.add(make("Ristorante Il Sorpasso",
                "🍽️ Restaurant", "amenity",
                41.9064, 12.4604, "Prati"));
        list.add(make("Hotel Hassler Roma",
                "🏨 Hotel", "tourism",
                41.9059, 12.4822, "Trinità dei Monti"));
        return setDistances(list, 41.9028, 12.4964);
    }

    // ─── BALI ────────────────────────────────────────────
    private static List<Recommendation> getBali() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Uluwatu Temple",
                "🏛️ Tourist Spot", "tourism",
                -8.8291, 115.0849, "Uluwatu"));
        list.add(make("Tegalalang Rice Terraces",
                "🏛️ Tourist Spot", "tourism",
                -8.4312, 115.2779, "Ubud"));
        list.add(make("Tanah Lot Temple",
                "🏛️ Tourist Spot", "tourism",
                -8.6215, 115.0868, "Tabanan"));
        list.add(make("Seminyak Beach",
                "🏛️ Tourist Spot", "tourism",
                -8.6897, 115.1548, "Seminyak"));
        list.add(make("Monkey Forest Ubud",
                "🏛️ Tourist Spot", "tourism",
                -8.5188, 115.2589, "Ubud"));
        list.add(make("Locavore Restaurant",
                "🍽️ Restaurant", "amenity",
                -8.5068, 115.2624, "Ubud"));
        list.add(make("Seasalt Restaurant",
                "🍽️ Restaurant", "amenity",
                -8.6778, 115.1598, "Seminyak"));
        list.add(make("Four Seasons Bali",
                "🏨 Hotel", "tourism",
                -8.5036, 115.2681, "Sayan, Ubud"));
        return setDistances(list, -8.4095, 115.1889);
    }

    // ─── SINGAPORE ───────────────────────────────────────
    private static List<Recommendation> getSingapore() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Gardens by the Bay",
                "🏛️ Tourist Spot", "tourism",
                1.2816, 103.8636, "Marina Bay"));
        list.add(make("Marina Bay Sands",
                "🏛️ Tourist Spot", "tourism",
                1.2834, 103.8607, "Bayfront Avenue"));
        list.add(make("Sentosa Island",
                "🏛️ Tourist Spot", "tourism",
                1.2494, 103.8303, "Sentosa"));
        list.add(make("Singapore Zoo",
                "🏛️ Tourist Spot", "tourism",
                1.4043, 103.7930, "Mandai"));
        list.add(make("Hawker Chan",
                "🍽️ Restaurant", "amenity",
                1.2789, 103.8439, "Chinatown"));
        list.add(make("Jumbo Seafood",
                "🍽️ Restaurant", "amenity",
                1.2897, 103.8559, "Clarke Quay"));
        list.add(make("Marina Bay Sands Hotel",
                "🏨 Hotel", "tourism",
                1.2834, 103.8607, "Bayfront Avenue"));
        return setDistances(list, 1.3521, 103.8198);
    }

    // ─── BANGKOK ─────────────────────────────────────────
    private static List<Recommendation> getBangkok() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Wat Phra Kaew",
                "🏛️ Tourist Spot", "tourism",
                13.7516, 100.4920, "Grand Palace"));
        list.add(make("Wat Arun",
                "🏛️ Tourist Spot", "tourism",
                13.7437, 100.4888, "Thonburi"));
        list.add(make("Chatuchak Market",
                "🏛️ Tourist Spot", "tourism",
                13.7999, 100.5500, "Chatuchak"));
        list.add(make("Khao San Road",
                "🏛️ Tourist Spot", "tourism",
                13.7588, 100.4973, "Banglamphu"));
        list.add(make("Gaggan Restaurant",
                "🍽️ Restaurant", "amenity",
                13.7447, 100.5418, "Langsuan Road"));
        list.add(make("Pad Thai Fai Ta Lu",
                "🍽️ Restaurant", "amenity",
                13.7483, 100.5014, "Old Town"));
        list.add(make("Mandarin Oriental Bangkok",
                "🏨 Hotel", "tourism",
                13.7236, 100.5142, "Charoen Krung Road"));
        return setDistances(list, 13.7563, 100.5018);
    }

    // ─── SYDNEY ──────────────────────────────────────────
    private static List<Recommendation> getSydney() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Sydney Opera House",
                "🏛️ Tourist Spot", "tourism",
                -33.8568, 151.2153, "Bennelong Point"));
        list.add(make("Sydney Harbour Bridge",
                "🏛️ Tourist Spot", "tourism",
                -33.8523, 151.2108, "The Rocks"));
        list.add(make("Bondi Beach",
                "🏛️ Tourist Spot", "tourism",
                -33.8908, 151.2743, "Bondi"));
        list.add(make("Taronga Zoo",
                "🏛️ Tourist Spot", "tourism",
                -33.8433, 151.2411, "Mosman"));
        list.add(make("Quay Restaurant",
                "🍽️ Restaurant", "amenity",
                -33.8594, 151.2099, "Circular Quay"));
        list.add(make("Icebergs Dining Room",
                "🍽️ Restaurant", "amenity",
                -33.8942, 151.2745, "Bondi Beach"));
        list.add(make("Park Hyatt Sydney",
                "🏨 Hotel", "tourism",
                -33.8574, 151.2118, "The Rocks"));
        return setDistances(list, -33.8688, 151.2093);
    }

    // ─── DELHI ───────────────────────────────────────────
    private static List<Recommendation> getDelhi() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Taj Mahal",
                "🏛️ Tourist Spot", "tourism",
                27.1751, 78.0421, "Agra (Day Trip)"));
        list.add(make("Red Fort",
                "🏛️ Tourist Spot", "tourism",
                28.6562, 77.2410, "Old Delhi"));
        list.add(make("Qutub Minar",
                "🏛️ Tourist Spot", "tourism",
                28.5245, 77.1855, "Mehrauli"));
        list.add(make("India Gate",
                "🏛️ Tourist Spot", "tourism",
                28.6129, 77.2295, "Rajpath"));
        list.add(make("Humayun's Tomb",
                "🏛️ Tourist Spot", "tourism",
                28.5933, 77.2507, "Nizamuddin"));
        list.add(make("Bukhara Restaurant",
                "🍽️ Restaurant", "amenity",
                28.5987, 77.1724, "ITC Maurya Hotel"));
        list.add(make("Karim's Restaurant",
                "🍽️ Restaurant", "amenity",
                28.6506, 77.2334, "Old Delhi"));
        list.add(make("The Lodhi Hotel",
                "🏨 Hotel", "tourism",
                28.5921, 77.2291, "Lodhi Road"));
        list.add(make("The Imperial Hotel",
                "🏨 Hotel", "tourism",
                28.6219, 77.2181, "Janpath"));
        return setDistances(list, 28.6139, 77.2090);
    }

    // ─── MUMBAI ──────────────────────────────────────────
    private static List<Recommendation> getMumbai() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Gateway of India",
                "🏛️ Tourist Spot", "tourism",
                18.9220, 72.8347, "Apollo Bunder"));
        list.add(make("Marine Drive",
                "🏛️ Tourist Spot", "tourism",
                18.9438, 72.8230, "Nariman Point"));
        list.add(make("Elephanta Caves",
                "🏛️ Tourist Spot", "tourism",
                18.9633, 72.9315, "Elephanta Island"));
        list.add(make("Chhatrapati Shivaji Terminus",
                "🏛️ Tourist Spot", "tourism",
                18.9398, 72.8354, "Fort"));
        list.add(make("Trishna Restaurant",
                "🍽️ Restaurant", "amenity",
                18.9317, 72.8340, "Fort"));
        list.add(make("Leopold Cafe",
                "🍽️ Restaurant", "amenity",
                18.9228, 72.8318, "Colaba"));
        list.add(make("Taj Mahal Palace Hotel",
                "🏨 Hotel", "tourism",
                18.9217, 72.8332, "Apollo Bunder"));
        return setDistances(list, 19.0760, 72.8777);
    }

    // ─── BARCELONA ───────────────────────────────────────
    private static List<Recommendation> getBarcelona() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Sagrada Família",
                "🏛️ Tourist Spot", "tourism",
                41.4036, 2.1744, "Eixample"));
        list.add(make("Park Güell",
                "🏛️ Tourist Spot", "tourism",
                41.4145, 2.1527, "Gràcia"));
        list.add(make("La Rambla",
                "🏛️ Tourist Spot", "tourism",
                41.3797, 2.1741, "Gothic Quarter"));
        list.add(make("Casa Batlló",
                "🏛️ Tourist Spot", "tourism",
                41.3916, 2.1650, "Passeig de Gràcia"));
        list.add(make("El Celler de Can Roca",
                "🍽️ Restaurant", "amenity",
                41.9961, 2.8193, "Girona"));
        list.add(make("Tickets Bar",
                "🍽️ Restaurant", "amenity",
                41.3755, 2.1581, "Eixample"));
        list.add(make("Hotel Arts Barcelona",
                "🏨 Hotel", "tourism",
                41.3870, 2.1964, "Port Olímpic"));
        return setDistances(list, 41.3851, 2.1734);
    }

    // ─── AMSTERDAM ───────────────────────────────────────
    private static List<Recommendation> getAmsterdam() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Anne Frank House",
                "🏛️ Tourist Spot", "tourism",
                52.3752, 4.8840, "Prinsengracht"));
        list.add(make("Rijksmuseum",
                "🏛️ Tourist Spot", "tourism",
                52.3600, 4.8852, "Museum Square"));
        list.add(make("Van Gogh Museum",
                "🏛️ Tourist Spot", "tourism",
                52.3584, 4.8811, "Museum Square"));
        list.add(make("Vondelpark",
                "🏛️ Tourist Spot", "tourism",
                52.3579, 4.8686, "Oud-West"));
        list.add(make("Restaurant Daalder",
                "🍽️ Restaurant", "amenity",
                52.3790, 4.8817, "Jordaan"));
        list.add(make("Hotel V Nesplein",
                "🏨 Hotel", "tourism",
                52.3699, 4.8937, "City Centre"));
        return setDistances(list, 52.3676, 4.9041);
    }

    // ─── ISTANBUL ────────────────────────────────────────
    private static List<Recommendation> getIstanbul() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Hagia Sophia",
                "🏛️ Tourist Spot", "tourism",
                41.0086, 28.9802, "Sultanahmet"));
        list.add(make("Blue Mosque",
                "🏛️ Tourist Spot", "tourism",
                41.0054, 28.9768, "Sultanahmet"));
        list.add(make("Topkapi Palace",
                "🏛️ Tourist Spot", "tourism",
                41.0115, 28.9833, "Sultanahmet"));
        list.add(make("Grand Bazaar",
                "🏛️ Tourist Spot", "tourism",
                41.0106, 28.9681, "Beyazıt"));
        list.add(make("Mikla Restaurant",
                "🍽️ Restaurant", "amenity",
                41.0338, 28.9774, "Beyoğlu"));
        list.add(make("Four Seasons Istanbul",
                "🏨 Hotel", "tourism",
                41.0082, 28.9786, "Sultanahmet"));
        return setDistances(list, 41.0082, 28.9784);
    }

    // ─── CAIRO ───────────────────────────────────────────
    private static List<Recommendation> getCairo() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Great Pyramid of Giza",
                "🏛️ Tourist Spot", "tourism",
                29.9792, 31.1342, "Giza Plateau"));
        list.add(make("Egyptian Museum",
                "🏛️ Tourist Spot", "tourism",
                30.0478, 31.2336, "Tahrir Square"));
        list.add(make("Sphinx",
                "🏛️ Tourist Spot", "tourism",
                29.9753, 31.1376, "Giza Plateau"));
        list.add(make("Khan el-Khalili Bazaar",
                "🏛️ Tourist Spot", "tourism",
                30.0477, 31.2624, "Islamic Cairo"));
        list.add(make("Naguib Mahfouz Cafe",
                "🍽️ Restaurant", "amenity",
                30.0466, 31.2622, "Khan el-Khalili"));
        list.add(make("Four Seasons Cairo",
                "🏨 Hotel", "tourism",
                30.0459, 31.2243, "Garden City"));
        return setDistances(list, 30.0444, 31.2357);
    }

    // ─── PRAGUE ──────────────────────────────────────────
    private static List<Recommendation> getPrague() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Prague Castle",
                "🏛️ Tourist Spot", "tourism",
                50.0911, 14.4000, "Hradčany"));
        list.add(make("Charles Bridge",
                "🏛️ Tourist Spot", "tourism",
                50.0865, 14.4114, "Staré Město"));
        list.add(make("Old Town Square",
                "🏛️ Tourist Spot", "tourism",
                50.0875, 14.4213, "Staré Město"));
        list.add(make("Astronomical Clock",
                "🏛️ Tourist Spot", "tourism",
                50.0870, 14.4202, "Old Town Hall"));
        list.add(make("Café Savoy",
                "🍽️ Restaurant", "amenity",
                50.0829, 14.4059, "Malá Strana"));
        list.add(make("Hotel Aria Prague",
                "🏨 Hotel", "tourism",
                50.0876, 14.4027, "Malá Strana"));
        return setDistances(list, 50.0755, 14.4378);
    }

    // ─── SAN FRANCISCO ───────────────────────────────────
    private static List<Recommendation> getSanFrancisco() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Golden Gate Bridge",
                "🏛️ Tourist Spot", "tourism",
                37.8199, -122.4783, "Golden Gate"));
        list.add(make("Alcatraz Island",
                "🏛️ Tourist Spot", "tourism",
                37.8267, -122.4230, "San Francisco Bay"));
        list.add(make("Fisherman's Wharf",
                "🏛️ Tourist Spot", "tourism",
                37.8080, -122.4177, "Fisherman's Wharf"));
        list.add(make("Golden Gate Park",
                "🏛️ Tourist Spot", "tourism",
                37.7694, -122.4862, "Richmond District"));
        list.add(make("Gary Danko Restaurant",
                "🍽️ Restaurant", "amenity",
                37.8057, -122.4225, "Fisherman's Wharf"));
        list.add(make("The Fairmont Hotel",
                "🏨 Hotel", "tourism",
                37.7924, -122.4108, "Nob Hill"));
        return setDistances(list, 37.7749, -122.4194);
    }

    // ─── NEW ZEALAND ─────────────────────────────────────
    private static List<Recommendation> getNewZealand() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Sky Tower Auckland",
                "🏛️ Tourist Spot", "tourism",
                -36.8484, 174.7622, "Auckland CBD"));
        list.add(make("Milford Sound",
                "🏛️ Tourist Spot", "tourism",
                -44.6413, 167.8974, "Fiordland"));
        list.add(make("Hobbiton Movie Set",
                "🏛️ Tourist Spot", "tourism",
                -37.8579, 175.6820, "Matamata"));
        list.add(make("Queenstown Gondola",
                "🏛️ Tourist Spot", "tourism",
                -45.0312, 168.6626, "Queenstown"));
        list.add(make("The French Café Auckland",
                "🍽️ Restaurant", "amenity",
                -36.8596, 174.7638, "Auckland"));
        list.add(make("Sofitel Auckland",
                "🏨 Hotel", "tourism",
                -36.8453, 174.7676, "Viaduct Harbour"));
        return setDistances(list, -36.8485, 174.7633);
    }

    // ─── MALDIVES ────────────────────────────────────────
    private static List<Recommendation> getMaldives() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("Banana Reef",
                "🏛️ Tourist Spot", "tourism",
                4.1755, 73.5093, "North Malé Atoll"));
        list.add(make("Male Fish Market",
                "🏛️ Tourist Spot", "tourism",
                4.1748, 73.5097, "Malé"));
        list.add(make("Hulhumale Beach",
                "🏛️ Tourist Spot", "tourism",
                4.2126, 73.5414, "Hulhumale"));
        list.add(make("Ithaa Undersea Restaurant",
                "🍽️ Restaurant", "amenity",
                3.9588, 72.8819, "Conrad Maldives"));
        list.add(make("Sea Fire Salt Restaurant",
                "🍽️ Restaurant", "amenity",
                4.1875, 73.5286, "Anantara Dhigu"));
        list.add(make("Conrad Maldives",
                "🏨 Hotel", "tourism",
                3.9588, 72.8819, "Rangali Island"));
        list.add(make("Gili Lankanfushi",
                "🏨 Hotel", "tourism",
                4.2775, 73.4297, "North Malé Atoll"));
        return setDistances(list, 4.1755, 73.5093);
    }

    // ─── DEFAULT fallback ─────────────────────────────────
    private static List<Recommendation> getDefault() {
        List<Recommendation> list = new ArrayList<>();
        list.add(make("City Museum",
                "🏛️ Tourist Spot", "tourism",
                0, 0, "City Centre"));
        list.add(make("Central Park / Garden",
                "🏛️ Tourist Spot", "tourism",
                0, 0, "Downtown"));
        list.add(make("Local Market",
                "🏛️ Tourist Spot", "tourism",
                0, 0, "Old Town"));
        list.add(make("Best Local Restaurant",
                "🍽️ Restaurant", "amenity",
                0, 0, "City Centre"));
        list.add(make("Rooftop Café",
                "🍽️ Restaurant", "amenity",
                0, 0, "Downtown"));
        list.add(make("City Centre Hotel",
                "🏨 Hotel", "tourism",
                0, 0, "City Centre"));
        return list;
    }

    // ─── HELPERS ─────────────────────────────────────────

    private static Recommendation make(String name,
                                       String category, String type,
                                       double lat, double lng, String address) {
        return new Recommendation(
                name, category, type, lat, lng, address);
    }

    // Calculate and set distances from city center
    private static List<Recommendation> setDistances(
            List<Recommendation> list,
            double cityLat, double cityLng) {
        for (Recommendation r : list) {
            if (r.getLatitude() != 0) {
                double dist = haversine(
                        cityLat, cityLng,
                        r.getLatitude(), r.getLongitude());
                r.setDistance(dist);
            }
        }
        return list;
    }

    private static double haversine(double lat1, double lng1,
                                    double lat2, double lng2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) *
                        Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        return R * 2 * Math.atan2(
                Math.sqrt(a), Math.sqrt(1-a));
    }
}