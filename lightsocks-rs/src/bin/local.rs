use tokio::{io, net::TcpStream, prelude::*};
fn main() {
    let addr = "127.0.0.1:4321".parse().unwrap();
    //tokio::net::TcpListener::bind(&addr);
    let hello = TcpStream::connect(&addr)
        .and_then(|stream| {
            println!("connected");
            io::write_all(stream, "hello world\n").then(|result| {
                println!("wrote to stream; success={:?}", result.is_ok());
                Ok(())
            })
        })
        .map_err(|err| {
            println!("failed to connect:{}", err);
        });
    tokio::run(hello);
}
