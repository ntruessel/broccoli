use actix_web::{App, HttpServer, web};
use anyhow::Result;
use sqlx::PgPool;

mod recipes;

#[actix_web::main]
async fn main() -> Result<()> {
    let pool = PgPool::connect("postgres://broccoli:password@localhost:15432/broccoli").await?;
    sqlx::migrate!("./migrations")
        .run(&pool)
        .await?;
    HttpServer::new(move || {
        App::new()
            .data(pool.clone())
            .service(web::scope("/recipes")
                .configure(recipes::config))
    })
        .bind("127.0.0.1:8080")?
        .run()
        .await?;
    Ok(())
}
