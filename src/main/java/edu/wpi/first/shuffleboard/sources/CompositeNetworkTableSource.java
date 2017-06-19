package edu.wpi.first.shuffleboard.sources;

import edu.wpi.first.shuffleboard.data.ComplexData;
import edu.wpi.first.shuffleboard.data.ComplexDataType;
import edu.wpi.first.shuffleboard.util.AsyncUtils;
import edu.wpi.first.shuffleboard.util.NetworkTableUtils;
import edu.wpi.first.wpilibj.tables.ITable;

import java.util.HashMap;
import java.util.Map;

/**
 * A network table source for composite data, ie data stored in multiple key-value pairs or nested
 * tables. The data is represented as a single map of keys (which may be multi-level ie "foo" or
 * "a/b" or "a/b/c/.../") to their values in the tables. This takes advantage of the fact that
 * network tables is a flat namespace and that a subtable is really just a shortcut for finding data
 * under a certain nested namespace.
 */
public class CompositeNetworkTableSource<D extends ComplexData<D>> extends NetworkTableSource<D> {

  private final Map<String, Object> backingMap = new HashMap<>();

  /**
   * Creates a composite network table source backed by the values associated with the given
   * subtable name.
   *
   * @param tableName the full path of the subtable backing this source
   * @param dataType  the data type for this source to accept
   */
  @SuppressWarnings("PMD.ConstructorCallsOverridableMethod") // PMD is dumb
  public CompositeNetworkTableSource(String tableName, ComplexDataType<D> dataType) {
    super(tableName);
    String path = NetworkTableUtils.normalizeKey(tableName, false);
    ITable table = NetworkTableUtils.rootTable.getSubTable(path);
    setData(dataType.getDefaultValue());

    setTableListener((key, value, flags) -> {
      AsyncUtils.runAsync(() -> {
        // make sure the updates run on the application thread
        boolean delete = NetworkTableUtils.isDelete(flags);
        String simpleKey = NetworkTableUtils.simpleKey(key);
        if (delete) {
          backingMap.remove(simpleKey);
        } else {
          backingMap.put(simpleKey, value);
        }
        setActive(NetworkTableUtils.dataTypeForEntry(fullTableKey) == dataType);
        setData(dataType.fromMap(backingMap));
      });
    });

    data.addListener((__, oldData, newData) -> {
      Map<String, Object> diff = newData.changesFrom(oldData);
      backingMap.putAll(diff);
      diff.forEach(table::putValue);
    });
  }

}