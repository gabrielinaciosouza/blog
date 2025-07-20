"use client";
import React from "react";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Sheet, SheetTrigger, SheetContent } from "@/components/ui/sheet";
import { Menu } from "lucide-react";

export default function NavbarClient({ isAdmin }: { isAdmin: boolean }) {
    const [open, setOpen] = React.useState(false);
    const handleMenuClick = () => setOpen(false);

    return (
        <header className="w-full">
            <nav className="fixed left-0 right-0 top-0 z-50 bg-background text-white flex flex-wrap items-center justify-between py-3 px-4 sm:px-8 gap-2 w-full border-b border-border" aria-label="Main Navigation" role="navigation">
                <Link href="/" className="flex items-center gap-2 min-w-0 flex-shrink">
                    <div className="flex items-center gap-2 min-w-0 flex-shrink transition-transform duration-300 hover:scale-105 hover:shadow-primary/40">
                        <img src="/logo2.png" alt="Logo" className="h-10 w-10 rounded-full bg-black" />
                        <span className="font-semibold text-lg tracking-normal truncate">Gabriel's Blog</span>
                    </div>
                </Link>
                <div className="hidden md:flex items-center gap-1 sm:gap-2 overflow-x-auto">
                    <Link href="/">
                        <Button variant="ghost">Homepage</Button>
                    </Link>
                    <Link href="/contact">
                        <Button variant="ghost">Contact</Button>
                    </Link>
                    <Link href="/about">
                        <Button variant="ghost">About</Button>
                    </Link>
                    {isAdmin && (
                        <Link href="/admin">
                            <Button variant="ghost">Admin</Button>
                        </Link>
                    )}
                </div>
                <div className="md:hidden flex items-center">
                    <Sheet open={open} onOpenChange={setOpen}>
                        <SheetTrigger asChild>
                            <Button variant="ghost" className="flex items-center gap-2 text-white">
                                <Menu className="w-6 h-6" />
                                <span className="text-base">Menu</span>
                            </Button>
                        </SheetTrigger>
                        <SheetContent side="right" className="bg-black border-0 shadow-none">
                            <div className="flex flex-col gap-4 mt-8">
                                <Link href="/" className="w-full" onClick={handleMenuClick}>
                                    <Button variant="ghost" className="w-full">Homepage</Button>
                                </Link>
                                <Link href="/contact" className="w-full" onClick={handleMenuClick}>
                                    <Button variant="ghost" className="w-full">Contact</Button>
                                </Link>
                                <Link href="/about" className="w-full" onClick={handleMenuClick}>
                                    <Button variant="ghost" className="w-full">About</Button>
                                </Link>
                                {isAdmin && (
                                    <Link href="/admin" className="w-full" onClick={handleMenuClick}>
                                        <Button variant="ghost" className="w-full">Admin</Button>
                                    </Link>
                                )}
                            </div>
                        </SheetContent>
                    </Sheet>
                </div>
            </nav>
            <div className="w-full h-px bg-border" />
        </header>
    );
}
