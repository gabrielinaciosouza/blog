import { Card, CardContent } from "@/components/ui/card";
import Image from "next/image";
import { Highlight } from "@/components/featured/Featured";

export default function AboutPage() {
    return (
        <div className="flex justify-center items-center min-h-[60vh] bg-background">
            <Card className="max-w-4xl w-full shadow-lg mx-4 md:mx-8">
                <CardContent>
                    <div className="flex flex-col md:flex-row gap-12 items-start">
                        <div className="group">
                            <Image
                                src="/profile-picture.png"
                                alt="Gabriel Inacio profile"
                                width={224}
                                height={224}
                                className="w-40 h-40 md:w-56 md:h-56 object-cover border-4 border-primary shadow-md rounded-full flex-shrink-0 transition-transform duration-300 group-hover:scale-105 group-hover:shadow-primary/40"
                                style={{ maxHeight: '14rem' }}
                                priority
                            />
                        </div>
                        <div className="flex-1 min-w-0">
                            <div className="mb-6">
                                <span className="text-3xl font-bold text-left block">About me</span>
                            </div>
                            <div className="prose prose-neutral dark:prose-invert max-w-none text-base mx-auto">
                                <p>
                                    I’m <Highlight>Gabriel Inacio</Highlight> — a{" "}
                                    <Highlight>software engineer</Highlight> passionate about solving real-world problems through{" "}
                                    <Highlight>clean code</Highlight>,{" "}
                                    <Highlight>scalable systems</Highlight>, and{" "}
                                    <Highlight>performance-first thinking</Highlight>. With over{" "}
                                    <Highlight>5 years of experience</Highlight> in backend development using{" "}
                                    <Highlight>Java</Highlight>, <Highlight>Spring Boot</Highlight>, and cloud platforms like{" "}
                                    <Highlight>GCP</Highlight> and <Highlight>Azure</Highlight>, I’ve led projects ranging from{" "}
                                    <Highlight>high-traffic marketplaces</Highlight> handling millions in transactions to{" "}
                                    <Highlight>banking systems</Highlight> processing billions of records with reliability and speed.
                                </p>
                                <div className="h-4" />
                                <p>
                                    But my journey didn’t start behind a screen — it started in the field. I worked as a{" "}
                                    <Highlight>powerline electrician</Highlight> before transitioning into tech. That hands-on background shaped how I build software:{" "}
                                    <Highlight>practical</Highlight>,{" "}
                                    <Highlight>efficient</Highlight>, and{" "}
                                    <Highlight>resilient under pressure</Highlight>.
                                </p>
                                <div className="h-4" />
                                <p>
                                    Today, I live in <Highlight>Poland</Highlight> and work at <Highlight>GPC Global</Highlight>, building{" "}
                                    <Highlight>mission-critical integrations</Highlight> between internal systems and major SaaS platforms. I’ve previously contributed to{" "}
                                    <Highlight>large-scale financial systems</Highlight> at <Highlight>UBS</Highlight> and helped launch{" "}
                                    <Highlight>Banco Next’s marketplace</Highlight> while at <Highlight>CI&T</Highlight>.
                                </p>
                                <div className="h-4" />
                                <p>
                                    This blog is my way of sharing what I learn in the trenches — from{" "}
                                    <Highlight>system architecture</Highlight> and{" "}
                                    <Highlight>cloud-native development</Highlight> to day-to-day lessons from real engineering work. If you&apos;re into{" "}
                                    <Highlight>thoughtful, real-world software discussions</Highlight>, you&apos;re in the right place.
                                </p>
                            </div>
                        </div>
                    </div>
                </CardContent>
            </Card>
        </div>
    );
}