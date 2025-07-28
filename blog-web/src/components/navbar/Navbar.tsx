import NavbarClient from "./NavbarClient";
import { cookies } from "next/headers";

export default async function Navbar() {
    let isAdmin = false;
    const cookie = await cookies();
    const authResponse = cookie.get("authResponse");
    if (authResponse?.value) {

        const parsed = JSON.parse(authResponse.value);
        if (parsed.role === "ADMIN") {
            isAdmin = true;
        }

    }
    return <NavbarClient isAdmin={isAdmin} />;
}
