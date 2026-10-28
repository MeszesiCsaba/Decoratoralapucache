# CachedContentSource

Java 17-es Maven-projekt, amely a Decorator mintával TTL-alapú cache-t ad egy ContentSource köré.

## Futtatás

    mvn test
    mvn package

A `--force` argumentum friss betöltést kér, és megkerüli az érvényes cache-t.

## Cache-szabály

A CachedContentSource a konstruktorban kapja a becsomagolt forrást, a TTL-t és a Clockot. TTL-en belül a mentett, nem módosítható listát adja vissza. Lejárat után újra meghívja a belső forrást. Ha ez hibát dob, a SourceException továbbterjed; lejárt adatot nem ad vissza. A cache műveletei szinkronizáltak, hogy párhuzamos olvasás ne indítson több egyidejű betöltést.

A force refresh a ForceRefresh interfészen keresztül történik. A NewsService külön kapja meg a ContentSource és a ForceRefresh képességet; nem használ instanceof ellenőrzést.

## Tesztek

A CachedContentSourceTest a TTL lejáratát, a frissítést, a hibakezelést, a lista módosíthatatlanságát és a párhuzamos olvasást ellenőrzi. A CachedContentSourceContractTest a b-17 szerződéstesztjeit futtatja a cache-dekorátoron is. A teszt-Clockkal nem kell valós időt várni.

## Miért Decorator?

A cache kompozícióval bármilyen ContentSource-t becsomagolhat. Így nem kell egy konkrét RSS-osztályból örökölni, és a cache új rétegként hozzáadható vagy eltávolítható.

## AI-használat

AI-t használtam a kód és a tesztötletek előkészítéséhez. 
