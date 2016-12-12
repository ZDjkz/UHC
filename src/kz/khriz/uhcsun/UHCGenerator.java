package kz.khriz.uhcsun;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class UHCGenerator extends ChunkGenerator {

    UHC UHC;
    public UHCGenerator(UHC instance) {
        UHC = instance;
    }

    public Location getFixedSpawnLocation(World world, Random random){
        return new Location(world, 0, 0, 0);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return new ArrayList<BlockPopulator>();
    }

    @SuppressWarnings("deprecation")
    public byte[][] generatorBlockSections(World world, Random random,
                                           int chunkX, int chunkY, BiomeGrid biomeGrid){
        byte[][] Result = new byte[256/16][];
        int x, y, z;

        for(x = 0; x<16; x++) {
            for(z = 0; z<16; z++) {
                setBlock(Result, x, 0, z, (byte) Material.BEDROCK.getId());
            }
        }

        for(x = 0; x<16; x++) {
            for(z = 0; z<16; z++) {
                for(y = 1;y<=2;y++){
                    setBlock(Result, x, y, z, (byte) Material.STONE.getId());
                }
            }
        }

        for(x = 0; x<16; x++) {
            for(z = 0; z<16; z++) {
                setBlock(Result, x, 3, z, (byte) Material.STONE.getId());
            }
        }

        return Result;
    }

    @SuppressWarnings("deprecation")
    @Override
    public short[][] generateExtBlockSections(World world, Random random,
                                              int chunkX, int chunkZ, BiomeGrid biomes) {
        short[][] Result = new short[256/16][];
        int x, y, z;

        for(x = 0; x<16; x++) {
            for(z = 0; z<16; z++) {
                setBlock(Result, x, 0, z, (short) Material.BEDROCK.getId());
            }
        }

        for(x = 0; x<16; x++) {
            for(z = 0; z<16; z++) {
                for(y = 1;y<=2;y++){
                    setBlock(Result, x, y, z, (short) Material.STONE.getId());
                }
            }
        }

        for(x = 0; x<16; x++) {
            for(z = 0; z<16; z++) {
                setBlock(Result, x, 3, z, (short) Material.STONE.getId());
            }
        }

        return Result;
    }

    private void setBlock(byte[][] result, int x, int y, int z, byte blockId) {
        if(result[y>>4] == null){
            result[y>>4] = new byte[4096];
        }
        result[y>>4][((y&0xF)<<8) | (z <<4) | x] = blockId;
    }

    private void setBlock(short[][] result, int x, int y, int z, short blockId) {
        if(result[y>>4] == null){
            result[y>>4] = new short[4096];
        }
        result[y>>4][((y&0xF)<<8) | (z <<4) | x] = blockId;
    }

}