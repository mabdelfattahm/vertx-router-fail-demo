package vertx.demo

import io.vertx.core.Vertx
import io.vertx.ext.web.openapi.RouterBuilder
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await

class MainVerticle: CoroutineVerticle() {
    override suspend fun start() {
        val router = routerFromYaml(this@MainVerticle.vertx, "src/main/resources/open-api.yaml")
        vertx
            .createHttpServer()
            .requestHandler(router.createRouter())
            .listen(8888)
            .onSuccess {
                println("HTTP server started on port 8888")
            }
            .onFailure {
                println("Failed ${it.cause}")
                it.printStackTrace()
            }.await()
    }
}

suspend fun routerFromYaml(vertx: Vertx, file: String): RouterBuilder {
    return RouterBuilder
        .create(vertx, file)
        .onSuccess {
            println("OpenAPI file \"$file\" was loaded successfully")
        }
        .onFailure {
            println("Failed to load OpenAPI file \"$file\": ${it.message}")
            throw it
        }
        .await()
}
