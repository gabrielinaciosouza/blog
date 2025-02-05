import type { Metadata } from "next";
import { Kanit } from "next/font/google";
import "./globals.css";
import Navbar from "@/components/navbar/Navbar";
import Footer from "@/components/footer/Footer";

const kanitBlack = Kanit({
    weight: ['100', '200', '300', '400', '500', '600', '700', '800', '900']
});

export const metadata: Metadata = {
  title: "Gabriel's blog",
  description: "The best blog ever!",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${kanitBlack.className}`}>
      <div className="container">
          <div className="wrapper">
              <Navbar/>
              {children}
              <Footer/>
          </div>
      </div>
      </body>
    </html>
  );
}
