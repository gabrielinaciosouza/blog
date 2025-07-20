import { Card, CardContent } from "@/components/ui/card";
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";


const Highlight = ({ children }: { children: React.ReactNode }) => (
  <span className="bg-gradient-to-r from-primary/20 to-secondary/20 px-1 rounded text-primary font-semibold">{children}</span>
);

const Featured = () => (
  <Card className="max-w-5xl mx-auto mt-24 p-0 bg-black rounded-2xl shadow-xl border border-border text-white">
    <CardContent className="flex flex-col md:flex-row items-center gap-10 p-10">
      <div className="flex flex-col items-center gap-4">
        <div className="group flex flex-col items-center">
          <Avatar className="w-40 h-40 border-4 border-primary shadow-md transition-transform duration-300 group-hover:scale-105 group-hover:shadow-primary/40 mb-6">
            <AvatarImage src="/profile-picture.png" alt="Gabriel's profile picture" />
            <AvatarFallback>G</AvatarFallback>
          </Avatar>
          <Badge
            variant="secondary"
            className="text-xs px-3 py-1 bg-muted text-white transition-transform duration-300 hover:scale-105 hover:shadow-primary/40"
          >
            Senior Software Engineer
          </Badge>
        </div>
        <Separator className="w-16 bg-muted mt-4" />
      </div>
      <div className="flex-1 text-center md:text-left">
        <h2 className="text-3xl font-extrabold mb-3 text-primary">Hi, I'm Gabriel</h2>
        <p className="text-muted-foreground text-lg leading-relaxed">
          A highly versatile and results-driven <Highlight>Senior Software Engineer</Highlight> with deep expertise in <Highlight>Java</Highlight>, <Highlight>Spring Boot</Highlight>, and <Highlight>Cloud (Azure &amp; GCP)</Highlight>. Passionate about building <Highlight>high-performance</Highlight>, <Highlight>scalable systems</Highlight>, I succeed in designing and developing solutions that handle <Highlight>intense data volumes</Highlight> while maintaining exceptional <Highlight>reliability</Highlight> and <Highlight>availability</Highlight>.
        </p>
      </div>
    </CardContent>
  </Card>
);

export default Featured;
