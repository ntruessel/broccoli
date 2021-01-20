use actix_web::{get, Responder, web};
use serde::Serialize;

#[derive(Serialize)]
struct Recipe {
    name: String
}

pub fn config(config: &mut web::ServiceConfig) {
    config.service(recipes);
}

#[get("")]
async fn recipes() -> impl Responder {
    web::Json(vec![Recipe { name: "Broccoli".to_string() }])
}
